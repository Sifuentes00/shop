package com.example.microshop.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String userId;

    private BigDecimal amount;

    private String status; // NEW, SUCCESS, FAILED

    private String method; // CARD, WALLET, ...

    private Instant createdAt;

    private String failureReason;

    // фабрика для успешной оплаты
    public static Payment success(Long orderId, String userId, BigDecimal amount, String method) {
        return Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .amount(amount)
                .method(method)
                .status("SUCCESS")
                .createdAt(Instant.now())
                .build();
    }

    // удобный фабричный метод, принимающий DTO
    public static Payment success(com.example.microshop.payment.dto.PaymentDto dto, String userId) {
        return success(dto.getOrderId(), userId, dto.getAmount(), dto.getMethod());
    }

    // фабрика для провала
    public static Payment failed(com.example.microshop.payment.dto.PaymentDto dto, String userId, String reason) {
        return Payment.builder()
                .orderId(dto != null ? dto.getOrderId() : null)
                .userId(userId)
                .amount(dto != null ? dto.getAmount() : null)
                .method(dto != null ? dto.getMethod() : null)
                .status("FAILED")
                .failureReason(reason)
                .createdAt(Instant.now())
                .build();
    }
}
