package com.example.microshop.order.client.service;

import com.example.microshop.order.client.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://product-service:8081/api/products";

    public BigDecimal getProductPrice(Long productId) {
        String token = extractBearerToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BigDecimal> response = restTemplate.exchange(
                baseUrl + "/" + productId + "/price",
                HttpMethod.GET,
                entity,
                BigDecimal.class
        );

        return response.getBody();
    }

    private String extractBearerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("No JWT token in security context");
    }
}

