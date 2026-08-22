package com.dmart.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "Total amount cannot be negative")
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Order status is required")
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Fulfillment type is required")
    private FulfillmentType fulfillmentType;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    private LocalDateTime scheduledDate;

    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
    }

    public Order(Long id,
                 double totalAmount,
                 OrderStatus status,
                 FulfillmentType fulfillmentType,
                 LocalDateTime orderDate,
                 LocalDateTime scheduledDate,
                 String deliveryAddress,
                 User user,
                 List<OrderItem> orderItems) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.fulfillmentType = fulfillmentType;
        this.orderDate = orderDate;
        this.scheduledDate = scheduledDate;
        this.deliveryAddress = deliveryAddress;
        this.user = user;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}