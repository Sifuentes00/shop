package com.example.microshop.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String userId;
    private String status;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private Instant updatedAt;

    @NotNull
    private List<OrderItemDto> items;
}

