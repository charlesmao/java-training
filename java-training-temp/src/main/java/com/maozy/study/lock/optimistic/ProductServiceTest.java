package com.maozy.study.lock.optimistic;

/**
 * Created by maozy on 2018/4/14.
 */
public class ProductServiceTest {

    public static void main(String[] args) throws InterruptedException {

        final ProductService productService = new ProductServiceImpl();

        final String productCode = "10001";
        Thread thread = new Thread(new Runnable() {
            public void run() {
                productService.updateGoodCAS(productCode, 3);
            }
        });

        thread.start();
        productService.updateGoodCAS(productCode, 2);
        thread.join();
        System.out.println(productService.getProduct(productCode));

    }

}
