package com.dmart.service;

import com.dmart.entity.Category;
import com.dmart.entity.Order;
import com.dmart.entity.OrderStatus;
import com.dmart.entity.Product;
import com.dmart.entity.Role;
import com.dmart.entity.User;
import com.dmart.exception.ResourceNotFoundException;
import com.dmart.repository.CategoryRepository;
import com.dmart.repository.OrderRepository;
import com.dmart.repository.ProductRepository;
import com.dmart.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // =========================================================
    // PRODUCT MANAGEMENT
    // =========================================================

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"));
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(
            Long productId,
            Product updatedProduct) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStockQuantity(
                updatedProduct.getStockQuantity()
        );
        product.setCategory(updatedProduct.getCategory());

        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        productRepository.delete(product);
    }


    // =========================================================
    // CATEGORY MANAGEMENT
    // =========================================================

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategory(Long categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found"));
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(
            Long categoryId,
            Category updatedCategory) {

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"));

        category.setName(updatedCategory.getName());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {

        Category category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"));

        categoryRepository.delete(category);
    }


    // =========================================================
    // USER MANAGEMENT
    // =========================================================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
    }

    @Transactional
    public User updateUserRole(
            Long userId,
            String role) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        String newRole =
                role.trim().toUpperCase();

        try {

            Role userRole =
                    Role.valueOf(newRole);

            user.setRole(userRole);

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Invalid role. Use CUSTOMER, STAFF or ADMIN"
            );
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        userRepository.delete(user);
    }


    // =========================================================
    // ORDER MANAGEMENT
    // =========================================================

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found"));
    }

    @Transactional
    public Order updateOrderStatus(
            Long orderId,
            OrderStatus status) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

        order.setStatus(status);

        return orderRepository.save(order);
    }
}