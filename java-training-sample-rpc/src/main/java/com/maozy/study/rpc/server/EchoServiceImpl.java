package com.maozy.study.rpc.server;

/**
 * 服务端接口实现
 *
 * Created by maozy on 2018/5/14.
 */
public class EchoServiceImpl implements EchoService {
    @Override
    public String echo(String ping) {
        return ping != null ? ping + "\nI am ok." : "I am ok.";
    }
}
