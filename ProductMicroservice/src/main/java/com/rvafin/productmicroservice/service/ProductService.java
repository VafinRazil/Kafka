package com.rvafin.productmicroservice.service;

import com.rvafin.productmicroservice.service.dto.CreateProductDTO;

import java.util.concurrent.ExecutionException;

public interface ProductService {
    String createProduct(CreateProductDTO createProductDTO) throws ExecutionException, InterruptedException;
}
