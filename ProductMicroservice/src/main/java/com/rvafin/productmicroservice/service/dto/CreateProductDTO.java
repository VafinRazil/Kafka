package com.rvafin.productmicroservice.service.dto;

import java.math.BigDecimal;

public class CreateProductDTO {
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public CreateProductDTO(){}

    public CreateProductDTO(String title, BigDecimal price, Integer quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
