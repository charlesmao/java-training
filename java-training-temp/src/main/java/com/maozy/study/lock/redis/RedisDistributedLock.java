package com.maozy.study.lock.optimistic.com.maozy.study.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DigestUtils;
import org.springframework.data.redis.core.script.RedisScript;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 *   Redis实现的分布式锁.
 *
 * 1、该实现为使用RedisTemplate泛型调用,可支持使用RedisTemplate或StringRedisTemplate传入；
 * 2、该实现在加锁(lock)与解锁（unlock）上都使用了lua脚本执行的方式，
 *    解决了在加锁（setnx key -> expire key）和解锁(get key -> del key)非原子性的问题；
 * 3、该实现在set key value中，value使用了uuid的方式解决了唯一性，以及“解铃还须系铃人”的特性，
 *    而非时间戳，避免了多台机器（多实现）部署时，时间不一致所造成的可能问题;
 * 4、该实现加锁可选用阻塞{@link #tryBlockingLock(int)}、 {@link #tryBlockingLock(int, long, int)}或非阻塞{@link #tryNonBlockingLock(int)}方式；
 * 5、该实现具有不可重入性，即不可重复调用tryLock()和unlock(),否则可能会抛异常；
 * 6、该实现非线程安全，应避免多线程情况下使用同一实例;
 * 7、该实现使用方式推荐如下：
 *       伪代码：
 *          RedisDistributedLock redisDistributedLock = new RedisDistributedLock();
 *          if (redisDistributedLock.tryLock()) {
 *              try {
 *                  doSomething();
 *              } catch () {
 *                  doOtherSomething();
 *              }finally {
 *                  redisDistributedLock.unlock();
 *              }
 *          }
 *
 * Created by maozy on 2018/2/7.
 */
public class RedisDistributedLock<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);

    private final RedisTemplate<K, V> redisTemplate;

    /**
     * 分布式锁的key,应与具体的业务对象相关
     */
    private final String lockKey;

    /**
     * 分布式锁的value,要保存唯一性
     */
    private final String lockValue;

    /**
     * 当前锁标志，主要用于重入时报错
     */
    private boolean locked = false;

    /**
     * 当前锁的时间戳
     */
    private long timeoutMillis;

	/**
	 *  当redisTemplate执行Lua脚本异常时，改用JedisCommands接口tryLock
	 */
	private JedisCommands jedisCommands;

	/**
	 * 参考 {@link redis.clients.jedis.Jedis#set(java.lang.String, java.lang.String, java.lang.String, java.lang.String, long) }
	 */
	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";

	/**
	 * 默认轮询的时间间隔(毫秒)
	 */
	private static long DEFAULT_TRY_INTERVAL_MILLIS = 60;

	/**
	 * 默认最大的轮询次数
	 */
	private static int DEFAULT_MAX_TRY_COUNT = 6;

    /**
     * 使用脚本在redis服务器执行这个逻辑可以在一定程度上保证此操作的原子性
     * （即不会发生客户端在执行setNX和expire命令之间，发生崩溃或失去与服务器的连接导致expire没有得到执行，发生永久死锁）
     * 除非脚本在redis服务器执行时redis服务器发生崩溃，不过此种情况锁也会失效
     */
    private static final RedisScript<Boolean> SETNX_AND_EXPIRE_SCRIPT;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('setnx', KEYS[1], ARGV[1]) == 1) then\n");
        sb.append("\tredis.call('expire', KEYS[1], tonumber(ARGV[2]))\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        SETNX_AND_EXPIRE_SCRIPT = new RedisScriptImpl(sb.toString(), Boolean.class);
    }

    private static final RedisScript<Boolean> DEL_IF_GET_EQUALS;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('get', KEYS[1]) == ARGV[1]) then\n");
        sb.append("\tredis.call('del', KEYS[1])\n");
        sb.append("\treturn true\n");
        sb.append("else\n");
        sb.append("\treturn false\n");
        sb.append("end");
        DEL_IF_GET_EQUALS = new RedisScriptImpl(sb.toString(), Boolean.class);
    }

    public RedisDistributedLock(RedisTemplate<K, V> redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.lockValue = UUID.randomUUID().toString();
    }


    private boolean doTryLock(int lockSeconds) throws Exception {
        if (locked) {
            throw new IllegalStateException("already locked!");
        }
		if (jedisCommands == null) {
			try {
				locked = redisTemplate.execute(SETNX_AND_EXPIRE_SCRIPT, (List<K>) Collections.singletonList(lockKey),
						lockValue, String.valueOf(lockSeconds));
			} catch (Exception e) {
				//JedisClusterConnection未对RedisScriptingCommands接口的evalSha()方法进行实现
				//集群情况下，上面方法redisTemplate.execute()会抛出异常，改为用JedisCommands接口处理
				Object object = redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
				if (object instanceof JedisCommands) {
					jedisCommands = (JedisCommands) object;
					return doTryLock(lockSeconds);
				} else {
					//Jedis只是redisTemplate底层的一种实现之一，以后如果改为Lettuce实现的话，这里请做扩展
					throw new ClassCastException("Not supported for " + object.getClass().getSimpleName());
				}
			}
		} else {
			//Jedis和JedisCluster都实现JedisCommands接口，set方法支持多参数设置(原子操作)
			String result = jedisCommands.set(lockKey, lockValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, lockSeconds);
			locked = LOCK_SUCCESS.equals(result);
		}

        if (locked) {
            this.timeoutMillis = System.currentTimeMillis() + lockSeconds * 1000;
        }
        return locked;
    }

    /**
	 * 尝试获得锁，成功返回true，如果失败立即返回false
	 * 此方式非阻塞，如果获取不到锁，会立即返回
	 *
	 * @param lockSeconds 加锁的时间(秒)，超过这个时间后锁会自动释放
	 */
	public boolean tryNonBlockingLock(int lockSeconds) {
        try {
            return doTryLock(lockSeconds);
        } catch (Exception e) {
			logger.info("tryLock Error", e);
            return false;
        }
    }

	/**
	 * 轮询的方式去获得锁，成功返回true，超过轮询次数或异常返回false.
	 * 此方式会阻塞，默认轮询{@link #DEFAULT_MAX_TRY_COUNT}次，每次间隔{@link #DEFAULT_TRY_INTERVAL_MILLIS}}毫秒
	 * 在不清楚方法执行时长的情况下,推荐使用.
	 * 
	 * @param lockSeconds
	 * @return
	 */
	public boolean tryBlockingLock(int lockSeconds) {
		return this.tryBlockingLock(lockSeconds, DEFAULT_TRY_INTERVAL_MILLIS, DEFAULT_MAX_TRY_COUNT);
	}


    /**
	 * 轮询的方式去获得锁，成功返回true，超过轮询次数或异常返回false
	 * 此方式会阻塞,轮询maxTryCount次，每次间隔tryIntervalMillis毫秒
	 * 在不清楚方法执行时长的情况下,不推荐使用.
	 * 
	 * @param lockSeconds       加锁的时间(秒)，超过这个时间后锁会自动释放
	 * @param tryIntervalMillis 轮询的时间间隔(毫秒)
	 * @param maxTryCount       最大的轮询次数
	 */
	public boolean tryBlockingLock(final int lockSeconds, final long tryIntervalMillis, final int maxTryCount) {
        int tryCount = 0;

        while (true) {
            if (++tryCount >= maxTryCount) {
                //获取锁超时
                return false;
            }

            try {
                if (doTryLock(lockSeconds)) {
                    return true;
                }
            } catch (Exception e) {
				logger.info("tryLock Error", e);
                return false;
            }

            try {
                Thread.sleep(tryIntervalMillis);
            } catch (InterruptedException e) {
				logger.info("tryLock interrupted", e);
                return false;
            }
        }
    }

    /**
     * 解锁操作
     */
    public void unlock() {
        if (!locked) {
            throw new IllegalStateException("not locked yet!");
        }

        locked = false;

        //如果已经超时，意味着锁已经失效，直接返回，减少一次网络调用
        if (System.currentTimeMillis() > this.timeoutMillis) {
            return;
        }

		if (jedisCommands == null) {
			redisTemplate.execute(DEL_IF_GET_EQUALS, (List<K>) Collections.singletonList(lockKey), lockValue);
		} else {
			if (jedisCommands instanceof Jedis) {
				Jedis jedis = (Jedis) jedisCommands;
				jedis.eval(DEL_IF_GET_EQUALS.getScriptAsString(), Collections.singletonList(lockKey),
						Collections.singletonList(lockValue));
			} else if (jedisCommands instanceof JedisCluster) {
				JedisCluster jedisCluster = (JedisCluster) jedisCommands;
				jedisCluster.eval(DEL_IF_GET_EQUALS.getScriptAsString(), Collections.singletonList(lockKey),
						Collections.singletonList(lockValue));
			}
		}

    }

    private static class RedisScriptImpl<T> implements RedisScript<T> {

        private final String script;
        private final String sha1;
        private final Class<T> resultType;

        public RedisScriptImpl(String script, Class<T> resultType) {
            this.script = script;
            this.sha1 = DigestUtils.sha1DigestAsHex(script);
            this.resultType = resultType;
        }

        public String getSha1() {
            return sha1;
        }

        public Class<T> getResultType() {
            return resultType;
        }

        public String getScriptAsString() {
            return script;
        }
    }

}
