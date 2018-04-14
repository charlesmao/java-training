package com.maozy.study.lock.optimistic;

/**
 * Created by maozy on 2018/4/14.
 */
public class ProductServiceImpl implements ProductService {
    /**模拟产品库存*/
    private static final Product PRODUCT = new Product("10001", "iPhone X", 10, 1);
    public Product getProduct(String productCode) {
        return PRODUCT;
    }
    public boolean updateGoodCAS(String produtCode, Integer decreaseNum) {
        Product product = getProduct(produtCode);
        System.out.println("线程" + Thread.currentThread().getName() + "获取产品信息：" + product);
        Integer remainingNumber = product.getRemainingNumber();
        Integer version = product.getVersion();
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        System.out.println("线程" + Thread.currentThread().getName() + "请求扣减库存量：" + decreaseNum);
        if (PRODUCT.getVersion() == version) { //版本号没变，才去扣减
            PRODUCT.setRemainingNumber(remainingNumber - decreaseNum);
            PRODUCT.setVersion(version + 1);
            System.out.println("线程" + Thread.currentThread().getName() + "扣减库存成功");
            return true;
        } else {
            System.out.println("线程" + Thread.currentThread().getName() + "扣减库存失败");
            return false;
        }
    }
}
