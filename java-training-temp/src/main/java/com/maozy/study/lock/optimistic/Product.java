package com.maozy.study.lock.optimistic;

/**
 * Created by maozy on 2018/4/14.
 */
public class Product {

    private String productCode;
    /**
     * 商品名字
     */
    private String productName;
    /**
     * 库存数量
     */
    private Integer remainingNumber;
    /*** 版本号*/
    private Integer version;

    @Override
    public String toString() {
        return "Product{" +
                "productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", remainingNumber=" + remainingNumber +
                ", version=" + version +
                '}';
    }

    public Product(String productCode, String productName, Integer remainingNumber, Integer version) {
        this.productCode = productCode;
        this.productName = productName;
        this.remainingNumber = remainingNumber;
        this.version = version;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getRemainingNumber() {
        return remainingNumber;
    }

    public void setRemainingNumber(Integer remainingNumber) {
        this.remainingNumber = remainingNumber;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
