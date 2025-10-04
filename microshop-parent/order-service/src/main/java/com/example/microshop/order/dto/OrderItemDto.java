package com.example.microshop.order.dto;

import lombok.Data;


@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Integer quantity;
}

