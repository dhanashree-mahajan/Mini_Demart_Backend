package com.dmart.controller;

import com.dmart.entity.Category;
import com.dmart.entity.Order;
import com.dmart.entity.OrderStatus;
import com.dmart.entity.Product;
import com.dmart.entity.User;
import com.dmart.service.AdminService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    // =========================================================
    // PRODUCTS
    // =========================================================

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(
                adminService.getAllProducts()
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                adminService.getProduct(productId)
        );
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(
            @RequestBody Product product) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        adminService.addProduct(product)
                );
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product product) {

        return ResponseEntity.ok(
                adminService.updateProduct(
                        productId,
                        product
                )
        );
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId) {

        adminService.deleteProduct(productId);

        return ResponseEntity.ok(
                "Product deleted successfully"
        );
    }


    // =========================================================
    // CATEGORIES
    // =========================================================

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {

        return ResponseEntity.ok(
                adminService.getAllCategories()
        );
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                adminService.getCategory(categoryId)
        );
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(
            @RequestBody Category category) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        adminService.addCategory(category)
                );
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody Category category) {

        return ResponseEntity.ok(
                adminService.updateCategory(
                        categoryId,
                        category
                )
        );
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long categoryId) {

        adminService.deleteCategory(categoryId);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }


    // =========================================================
    // USERS
    // =========================================================

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                adminService.getAllUsers()
        );
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                adminService.getUser(userId)
        );
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestParam String role) {

        return ResponseEntity.ok(
                adminService.updateUserRole(
                        userId,
                        role
                )
        );
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long userId) {

        adminService.deleteUser(userId);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }


    // =========================================================
    // ORDERS
    // =========================================================

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {

        return ResponseEntity.ok(
                adminService.getAllOrders()
        );
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                adminService.getOrder(orderId)
        );
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(
                adminService.updateOrderStatus(
                        orderId,
                        status
                )
        );
    }
}