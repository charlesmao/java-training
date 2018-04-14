package com.maozy.study.lock.optimistic;

/**
 * Created by maozy on 2018/4/14.
 */
public interface ProductService {

    /**
     * 获取产品
     * @return
     */
    Product getProduct(String productCode);

    /**
     * 扣减库存
     * @param produtCode 产品编码
     * @param decreaseNum 扣减数量
     */
    boolean updateGoodCAS(String produtCode, Integer decreaseNum);

}
