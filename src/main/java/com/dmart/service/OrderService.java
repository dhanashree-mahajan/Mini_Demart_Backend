package com.dmart.service;

import com.dmart.entity.Order;
import com.dmart.entity.OrderItem;
import com.dmart.entity.OrderStatus;
import com.dmart.entity.Product;
import com.dmart.exception.ResourceNotFoundException;
import com.dmart.repository.OrderRepository;
import com.dmart.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // Get all orders
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    // Get order by ID
    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));
    }

    // Get orders by user
    public List<Order> getOrdersByUser(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    // Update order status
    @Transactional
    public Order updateOrderStatus(
            Long orderId,
            OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        OrderStatus currentStatus =
                order.getStatus();

        validateStatusTransition(
                currentStatus,
                newStatus
        );

        order.setStatus(newStatus);

        return orderRepository.save(order);
    }

    // Cancel order
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));

        OrderStatus currentStatus =
                order.getStatus();

        // Only these statuses can be cancelled
        if (currentStatus != OrderStatus.PENDING
                && currentStatus != OrderStatus.CONFIRMED
                && currentStatus != OrderStatus.PREPARING
                && currentStatus != OrderStatus.READY_FOR_PICKUP) {

            throw new IllegalArgumentException(
                    "Order cannot be cancelled in "
                            + currentStatus + " status");
        }

        // Restore stock
        if (order.getOrderItems() != null) {

            for (OrderItem orderItem :
                    order.getOrderItems()) {

                Product product =
                        orderItem.getProduct();

                product.setStockQuantity(
                        product.getStockQuantity()
                                + orderItem.getQuantity()
                );

                productRepository.save(product);
            }
        }

        // Change status
        order.setStatus(
                OrderStatus.CANCELLED
        );

        return orderRepository.save(order);
    }

    // Validate order status transitions
    private void validateStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus) {

        // Same status
        if (currentStatus == newStatus) {

            throw new IllegalArgumentException(
                    "Order is already in "
                            + newStatus + " status");
        }

        // Cancelled cannot be changed
        if (currentStatus ==
                OrderStatus.CANCELLED) {

            throw new IllegalArgumentException(
                    "Cancelled order cannot be updated");
        }

        // Delivered cannot be changed
        if (currentStatus ==
                OrderStatus.DELIVERED) {

            throw new IllegalArgumentException(
                    "Delivered order cannot be updated");
        }

        // Picked up cannot be changed
        if (currentStatus ==
                OrderStatus.PICKED_UP) {

            throw new IllegalArgumentException(
                    "Picked up order cannot be updated");
        }

        // --------------------------------
        // PENDING
        // --------------------------------
        if (currentStatus ==
                OrderStatus.PENDING) {

            if (newStatus !=
                    OrderStatus.CONFIRMED
                    && newStatus !=
                    OrderStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Pending order can only be "
                                + "CONFIRMED or CANCELLED");
            }

            return;
        }

        // --------------------------------
        // CONFIRMED
        // --------------------------------
        if (currentStatus ==
                OrderStatus.CONFIRMED) {

            if (newStatus !=
                    OrderStatus.PREPARING
                    && newStatus !=
                    OrderStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Confirmed order can only be "
                                + "PREPARING or CANCELLED");
            }

            return;
        }

        // --------------------------------
        // PREPARING
        // --------------------------------
        if (currentStatus ==
                OrderStatus.PREPARING) {

            if (newStatus !=
                    OrderStatus.READY_FOR_PICKUP
                    && newStatus !=
                    OrderStatus.OUT_FOR_DELIVERY
                    && newStatus !=
                    OrderStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Preparing order can only be "
                                + "READY_FOR_PICKUP, "
                                + "OUT_FOR_DELIVERY or CANCELLED");
            }

            return;
        }

        // --------------------------------
        // READY FOR PICKUP
        // --------------------------------
        if (currentStatus ==
                OrderStatus.READY_FOR_PICKUP) {

            if (newStatus !=
                    OrderStatus.PICKED_UP
                    && newStatus !=
                    OrderStatus.CANCELLED) {

                throw new IllegalArgumentException(
                        "Ready for pickup order can only be "
                                + "PICKED_UP or CANCELLED");
            }

            return;
        }

        // --------------------------------
        // OUT FOR DELIVERY
        // --------------------------------
        if (currentStatus ==
                OrderStatus.OUT_FOR_DELIVERY) {

            if (newStatus !=
                    OrderStatus.DELIVERED) {

                throw new IllegalArgumentException(
                        "Out for delivery order can only be "
                                + "DELIVERED");
            }

            return;
        }
    }
}