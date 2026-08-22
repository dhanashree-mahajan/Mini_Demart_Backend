package com.dmart.service;

import com.dmart.entity.Order;
import com.dmart.entity.OrderItem;
import com.dmart.entity.Product;
import com.dmart.entity.User;
import com.dmart.exception.ResourceNotFoundException;
import com.dmart.repository.OrderItemRepository;
import com.dmart.repository.OrderRepository;
import com.dmart.repository.ProductRepository;
import com.dmart.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ================= CREATE ORDER =================

    public Order addOrder(Order order) {

        Long userId = order.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        order.setUser(user);

        return orderRepository.save(order);
    }

    // ================= GET ALL ORDERS =================

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    // ================= GET ORDER BY ID =================

    public Order getOrderById(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));
    }

    // ================= GET ORDERS BY USER =================

    public List<Order> getOrdersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user);
    }

    // ================= UPDATE ORDER =================

    public Order updateOrder(Long id, Order order) {

        Order existingOrder = getOrderById(id);

        existingOrder.setTotalAmount(order.getTotalAmount());
        existingOrder.setStatus(order.getStatus());
        existingOrder.setFulfillmentType(order.getFulfillmentType());
        existingOrder.setOrderDate(order.getOrderDate());
        existingOrder.setScheduledDate(order.getScheduledDate());
        existingOrder.setDeliveryAddress(order.getDeliveryAddress());

        Long userId = order.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        existingOrder.setUser(user);

        return orderRepository.save(existingOrder);
    }

    // ================= DELETE ORDER =================

    public void deleteOrder(Long id) {

        Order order = getOrderById(id);

        orderRepository.delete(order);
    }

    // ================= ADD ORDER ITEM =================

    public OrderItem addOrderItem(
            Long orderId,
            Long productId,
            int quantity) {

        Order order = getOrderById(orderId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock");
        }

        OrderItem orderItem = new OrderItem();

        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());

        // Reduce product stock
        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );

        productRepository.save(product);

        OrderItem savedItem = orderItemRepository.save(orderItem);

        calculateTotalAmount(order);

        return savedItem;
    }

    // ================= GET ORDER ITEMS =================

    public List<OrderItem> getOrderItems(Long orderId) {

        Order order = getOrderById(orderId);

        return orderItemRepository.findByOrder(order);
    }

    // ================= DELETE ORDER ITEM =================

    public void deleteOrderItem(Long orderItemId) {

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order item not found"));

        Order order = orderItem.getOrder();

        Product product = orderItem.getProduct();

        // Return quantity back to stock
        product.setStockQuantity(
                product.getStockQuantity()
                        + orderItem.getQuantity()
        );

        productRepository.save(product);

        // Delete order item
        orderItemRepository.delete(orderItem);

        // Recalculate order total
        calculateTotalAmount(order);
    }

    // ================= CALCULATE ORDER TOTAL =================

    private void calculateTotalAmount(Order order) {

        List<OrderItem> items =
                orderItemRepository.findByOrder(order);

        double total = 0;

        for (OrderItem item : items) {

            total += item.getPrice()
                    * item.getQuantity();
        }

        order.setTotalAmount(total);

        orderRepository.save(order);
    }
}