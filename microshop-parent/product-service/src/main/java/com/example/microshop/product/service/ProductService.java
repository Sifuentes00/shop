package com.example.microshop.product.service;

import com.example.microshop.product.dto.ProductDto;
import com.example.microshop.product.entity.Product;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto product);
    ProductDto update(Long id, ProductDto product);
    ProductDto findById(Long id);
    List<ProductDto> findAll();
    void delete(Long id);
    public List<Product> searchProductsByName(String name);
}
