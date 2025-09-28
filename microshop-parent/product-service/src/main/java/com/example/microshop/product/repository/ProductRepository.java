package com.example.microshop.product.repository;

import com.example.microshop.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // можно добавить поиск по имени/фильтры позже
}

