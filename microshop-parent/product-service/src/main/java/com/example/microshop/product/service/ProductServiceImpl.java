package com.example.microshop.product.service;

import com.example.microshop.product.dto.ProductDto;
import com.example.microshop.product.entity.Product;
import com.example.microshop.product.exception.ResourceNotFoundException;
import com.example.microshop.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    private ProductDto toDto(Product p) {
        ProductDto d = new ProductDto();
        d.setId(p.getId());
        d.setName(p.getName());
        d.setDescription(p.getDescription());
        d.setPrice(p.getPrice());
        d.setStockQuantity(p.getStockQuantity());
        return d;
    }

    private Product fromDto(ProductDto d) {
        Product p = new Product();
        p.setName(d.getName());
        p.setDescription(d.getDescription());
        p.setPrice(d.getPrice());
        p.setStockQuantity(d.getStockQuantity());
        return p;
    }

    @Override
    public ProductDto create(ProductDto product) {
        Product p = fromDto(product);
        Product saved = repository.save(p);
        return toDto(saved);
    }

    @Override
    public ProductDto update(Long id, ProductDto product) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        // update fields
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        Product saved = repository.save(existing);
        return toDto(saved);
    }

    @Override
    public ProductDto findById(Long id) {
        Product p = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
        return toDto(p);
    }

    @Override
    public List<ProductDto> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id " + id);
        }
        repository.deleteById(id);
    }
}
