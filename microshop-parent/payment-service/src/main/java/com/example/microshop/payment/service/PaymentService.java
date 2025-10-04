package com.example.microshop.payment.service;

import com.example.microshop.payment.dto.PaymentDto;
import com.example.microshop.payment.entity.Payment;
import com.example.microshop.payment.repository.PaymentRepository;
import com.example.microshop.payment.client.OrderClient;
import com.example.microshop.payment.client.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public Payment processPayment(PaymentDto dto, String userId) {
        // 1) Если orderClient доступен — проверим заказ
        if (orderClient != null) {
            try {
                OrderDto order = orderClient.getOrderById(dto.getOrderId());
                if (order == null) {
                    return paymentRepository.save(Payment.failed(dto, userId, "Order not found"));
                }
                if (!userId.equals(order.getUserId())) {
                    return paymentRepository.save(Payment.failed(dto, userId, "Order does not belong to user"));
                }
                if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(dto.getAmount()) != 0) {
                    return paymentRepository.save(Payment.failed(dto, userId, "Amount mismatch"));
                }

                Payment payment = Payment.success(dto, userId);
                payment = paymentRepository.save(payment);


                try {
                    orderClient.updateOrderStatus(order.getId(), "PAID");
                } catch (Exception e) {

                }

                return payment;

            } catch (Exception ex) {

                return paymentRepository.save(Payment.failed(dto, userId, "Error contacting order service: " + ex.getMessage()));
            }
        }

        Payment payment = Payment.builder()
                .orderId(dto.getOrderId())
                .userId(userId)
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .status("PENDING")
                .createdAt(Instant.now())
                .build();

        return paymentRepository.save(payment);
    }
}
