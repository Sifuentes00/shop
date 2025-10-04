package com.example.microshop.payment.client;

import com.example.microshop.payment.client.dto.OrderDto;
import com.example.microshop.payment.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "${order.service.url:http://order-service:8082}", configuration = FeignConfig.class)
public interface OrderClient {

    @GetMapping("/api/orders/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    OrderDto getOrderById(@PathVariable("id") Long id);

    @PutMapping("/api/orders/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}
