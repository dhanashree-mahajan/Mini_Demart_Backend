package com.dmart.controller;

import com.dmart.entity.Cart;
import com.dmart.entity.CartItem;
import com.dmart.service.CartService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ================= CREATE CART =================

    @PostMapping("/user/{userId}")
    public ResponseEntity<Cart> createCart(
            @PathVariable Long userId) {

        Cart cart = cartService.createCart(userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cart);
    }

    // ================= GET CART BY USER =================

    @GetMapping("/user/{userId}")
    public ResponseEntity<Cart> getCartByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCartByUser(userId)
        );
    }

    // ================= GET CART BY ID =================

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(
            @PathVariable Long cartId) {

        return ResponseEntity.ok(
                cartService.getCartById(cartId)
        );
    }

    // ================= ADD PRODUCT =================

    @PostMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<CartItem> addToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity) {

        CartItem cartItem =
                cartService.addToCart(
                        userId,
                        productId,
                        quantity
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItem);
    }

    // ================= GET CART ITEMS =================

    @GetMapping("/user/{userId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCartItems(userId)
        );
    }

    // ================= UPDATE CART ITEM =================

    @PutMapping(
            "/user/{userId}/items/{cartItemId}"
    )
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(
                cartService.updateCartItem(
                        userId,
                        cartItemId,
                        quantity
                )
        );
    }

    // ================= REMOVE CART ITEM =================

    @DeleteMapping(
            "/user/{userId}/items/{cartItemId}"
    )
    public ResponseEntity<String> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {

        cartService.removeFromCart(
                userId,
                cartItemId
        );

        return ResponseEntity.ok(
                "Cart item removed successfully"
        );
    }

    // ================= CLEAR CART =================

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<String> clearCart(
            @PathVariable Long userId) {

        cartService.clearCart(userId);

        return ResponseEntity.ok(
                "Cart cleared successfully"
        );
    }
}