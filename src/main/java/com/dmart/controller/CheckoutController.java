package com.dmart.controller;

import com.dmart.dto.CheckoutRequest;
import com.dmart.entity.Order;
import com.dmart.service.CheckoutService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(
            CheckoutService checkoutService) {

        this.checkoutService = checkoutService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Order> checkout(
            @PathVariable Long userId,
            @Valid @RequestBody CheckoutRequest request) {

        Order order =
                checkoutService.checkout(
                        userId,
                        request
                );

        return ResponseEntity.ok(order);
    }
}