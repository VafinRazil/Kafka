package com.rvafin.core;

import java.math.BigDecimal;

public class ProductCreatedEvent {
    private String title;
    private BigDecimal price;
    private Integer quantity;
    private String productId;

    public ProductCreatedEvent() {
    }

    public ProductCreatedEvent(String title, BigDecimal price, Integer quantity, String productId) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
