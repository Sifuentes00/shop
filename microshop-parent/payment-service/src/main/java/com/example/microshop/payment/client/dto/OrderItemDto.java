package com.example.microshop.payment.client.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;  // или можно убрать, если цена только в ProductService
}
