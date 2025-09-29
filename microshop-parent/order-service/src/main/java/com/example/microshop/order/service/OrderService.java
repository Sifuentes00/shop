package com.example.microshop.order.service;

import com.example.microshop.order.dto.OrderDto;
import com.example.microshop.order.dto.OrderItemDto;
import com.example.microshop.order.entity.Order;
import com.example.microshop.order.entity.OrderItem;
import com.example.microshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id).map(this::toDto);
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.getUserId()); // пока руками, позже возьмем из токена
        order.setStatus("NEW");
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());

        List<OrderItem> items = orderDto.getItems().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(dto.getProductId());
                    item.setQuantity(dto.getQuantity());
                    item.setOrder(order);
                    return item;
                })
                .toList();

        order.setItems(items);

        // пересчет суммы (заглушка — 100 за штуку)
        BigDecimal total = items.stream()
                .map(i -> BigDecimal.valueOf(i.getQuantity() * 100))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        return toDto(orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    // ---------- Мапперы ----------

    private OrderDto toDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
