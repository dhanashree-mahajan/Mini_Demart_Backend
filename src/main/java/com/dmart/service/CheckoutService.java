package com.dmart.service;

import com.dmart.dto.CheckoutRequest;

import com.dmart.entity.Cart;
import com.dmart.entity.CartItem;
import com.dmart.entity.FulfillmentType;
import com.dmart.entity.Order;
import com.dmart.entity.OrderItem;
import com.dmart.entity.OrderStatus;
import com.dmart.entity.Product;
import com.dmart.entity.User;

import com.dmart.exception.ResourceNotFoundException;

import com.dmart.repository.CartItemRepository;
import com.dmart.repository.CartRepository;
import com.dmart.repository.OrderItemRepository;
import com.dmart.repository.OrderRepository;
import com.dmart.repository.ProductRepository;
import com.dmart.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CheckoutService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order checkout(
            Long userId,
            CheckoutRequest request) {

        // Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        // Find cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));

        // Get cart items
        List<CartItem> cartItems =
                cartItemRepository.findByCart(cart);

        // Cart cannot be empty
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cart is empty");
        }

        // Convert fulfillment type
        FulfillmentType fulfillmentType;

        try {

            fulfillmentType =
                    FulfillmentType.valueOf(
                            request.getFulfillmentType()
                                    .trim()
                                    .toUpperCase());

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid fulfillment type. Use STORE_PICKUP, "
                            + "SCHEDULED_PICKUP or HOME_DELIVERY");
        }

        // Validate fulfillment details
        validateFulfillment(
                fulfillmentType,
                request
        );

        // Check stock before creating order
        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();

            if (product.getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getName());
            }
        }

        // Create Order
        Order order = new Order();

        order.setUser(user);

        order.setStatus(
                OrderStatus.PENDING
        );

        order.setFulfillmentType(
                fulfillmentType
        );

        order.setOrderDate(
                LocalDateTime.now()
        );

        order.setScheduledDate(
                request.getScheduledDate()
        );

        order.setDeliveryAddress(
                request.getDeliveryAddress()
        );

        order.setTotalAmount(0.0);

        Order savedOrder =
                orderRepository.save(order);

        double totalAmount = 0.0;

        // Create OrderItems
        for (CartItem cartItem : cartItems) {

            Product product =
                    cartItem.getProduct();

            int quantity =
                    cartItem.getQuantity();

            double price =
                    product.getPrice();

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(
                    savedOrder
            );

            orderItem.setProduct(
                    product
            );

            orderItem.setQuantity(
                    quantity
            );

            orderItem.setPrice(
                    price
            );

            // Save OrderItem
            OrderItem savedOrderItem =
                    orderItemRepository.save(
                            orderItem
                    );

            // IMPORTANT:
            // Add the saved item to the Order's list
            savedOrder.getOrderItems()
                    .add(savedOrderItem);

            // Reduce product stock
            product.setStockQuantity(
                    product.getStockQuantity()
                            - quantity
            );

            productRepository.save(product);

            // Calculate total
            totalAmount +=
                    price * quantity;
        }

        // Set final total
        savedOrder.setTotalAmount(
                totalAmount
        );

        // Save order again
        savedOrder =
                orderRepository.save(
                        savedOrder
                );

        // Clear cart
        cartItemRepository.deleteAll(
                cartItems
        );

        return savedOrder;
    }


    private void validateFulfillment(
            FulfillmentType fulfillmentType,
            CheckoutRequest request) {

        // STORE PICKUP
        if (fulfillmentType ==
                FulfillmentType.STORE_PICKUP) {

            return;
        }

        // SCHEDULED PICKUP
        if (fulfillmentType ==
                FulfillmentType.SCHEDULED_PICKUP) {

            if (request.getScheduledDate() == null) {

                throw new IllegalArgumentException(
                        "Scheduled date is required for scheduled pickup");
            }

            if (request.getScheduledDate()
                    .isBefore(LocalDateTime.now())) {

                throw new IllegalArgumentException(
                        "Scheduled date must be in the future");
            }

            return;
        }

        // HOME DELIVERY
        if (fulfillmentType ==
                FulfillmentType.HOME_DELIVERY) {

            if (request.getDeliveryAddress() == null
                    || request.getDeliveryAddress()
                    .trim()
                    .isEmpty()) {

                throw new IllegalArgumentException(
                        "Delivery address is required for home delivery");
            }
        }
    }
}