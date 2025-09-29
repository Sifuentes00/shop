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
    // OrderClient будет интерфейсом REST-клиента (Feign/WebClient). Может быть null пока не подключим.
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

                // все проверки пройдены — создаём payment SUCCESS
                Payment payment = Payment.success(dto, userId);
                payment = paymentRepository.save(payment);

                // уведомляем order-service — обновляем статус
                try {
                    orderClient.updateOrderStatus(order.getId(), "PAID");
                } catch (Exception e) {
                    // не критично: оплата сохранена, но заказ не обновлён — логируем и возвращаем SUCCESS
                    // (в реале: retry, saga, или событие в очередь)
                    // logger.warn("Failed to notify order-service: {}", e.getMessage());
                }

                return payment;

            } catch (Exception ex) {
                // если при обращении в order-service что-то пошло не так — сохраняем failed с причиной
                return paymentRepository.save(Payment.failed(dto, userId, "Error contacting order service: " + ex.getMessage()));
            }
        }

        // 2) Если orderClient не настроен — fallback: создаём платёж, пометим PENDING или SUCCESS по твоему выбору.
        // Для учебного проекта можно пометить SUCCESS (но лучше PENDING).
        Payment payment = Payment.builder()
                .orderId(dto.getOrderId())
                .userId(userId)
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .status("PENDING") // или "SUCCESS" — на твой выбор
                .createdAt(Instant.now())
                .build();

        return paymentRepository.save(payment);
    }
}
