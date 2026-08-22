package com.dmart.controller;

import com.dmart.entity.Order;
import com.dmart.entity.OrderItem;
import com.dmart.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ================= CREATE ORDER =================

    @PostMapping
    public ResponseEntity<Order> addOrder(
            @Valid @RequestBody Order order) {

        Order savedOrder = orderService.addOrder(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrder);
    }

    // ================= GET ALL ORDERS =================

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    // ================= GET ORDER BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    // ================= GET ORDERS BY USER =================

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                orderService.getOrdersByUser(userId)
        );
    }

    // ================= GET ORDER ITEMS =================

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderItems(orderId)
        );
    }

    // ================= UPDATE ORDER =================

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody Order order) {

        return ResponseEntity.ok(
                orderService.updateOrder(id, order)
        );
    }

    // ================= DELETE ORDER =================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id) {

        orderService.deleteOrder(id);

        return ResponseEntity.ok(
                "Order deleted successfully"
        );
    }

    // ================= ADD ORDER ITEM =================

    @PostMapping("/{orderId}/items/{productId}")
    public ResponseEntity<OrderItem> addOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long productId,
            @RequestParam int quantity) {

        OrderItem savedItem = orderService.addOrderItem(
                orderId,
                productId,
                quantity
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedItem);
    }

    // ================= DELETE ORDER ITEM =================

    @DeleteMapping("/items/{orderItemId}")
    public ResponseEntity<String> deleteOrderItem(
            @PathVariable Long orderItemId) {

        orderService.deleteOrderItem(orderItemId);

        return ResponseEntity.ok(
                "Order item deleted successfully"
        );
    }
}