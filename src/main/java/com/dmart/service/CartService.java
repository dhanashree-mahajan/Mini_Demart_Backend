package com.dmart.service;

import com.dmart.entity.Cart;
import com.dmart.entity.CartItem;
import com.dmart.entity.Product;
import com.dmart.entity.User;

import com.dmart.exception.ResourceNotFoundException;

import com.dmart.repository.CartItemRepository;
import com.dmart.repository.CartRepository;
import com.dmart.repository.ProductRepository;
import com.dmart.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ================= CREATE CART =================

    public Cart createCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        if (cartRepository.existsByUser(user)) {
            throw new RuntimeException(
                    "Cart already exists for this user");
        }

        Cart cart = new Cart();

        cart.setUser(user);

        return cartRepository.save(cart);
    }

    // ================= GET CART BY USER =================

    public Cart getCartByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));
    }

    // ================= GET CART BY ID =================

    public Cart getCartById(Long cartId) {

        return cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart not found"));
    }

    // ================= ADD PRODUCT TO CART =================

    public CartItem addToCart(
            Long userId,
            Long productId,
            int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {

                    Cart newCart = new Cart();

                    newCart.setUser(user);

                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock");
        }

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProductId(
                                cart,
                                productId
                        )
                        .orElse(null);

        if (cartItem != null) {

            int newQuantity =
                    cartItem.getQuantity() + quantity;

            if (product.getStockQuantity() < newQuantity) {
                throw new IllegalArgumentException(
                        "Insufficient stock");
            }

            cartItem.setQuantity(newQuantity);

        } else {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        return cartItemRepository.save(cartItem);
    }

    // ================= GET CART ITEMS =================

    public List<CartItem> getCartItems(Long userId) {

        Cart cart = getCartByUser(userId);

        return cartItemRepository.findByCart(cart);
    }

    // ================= UPDATE CART ITEM =================

    public CartItem updateCartItem(
            Long userId,
            Long cartItemId,
            int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        Cart cart = getCartByUser(userId);

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"));

        if (!cartItem.getCart().getId()
                .equals(cart.getId())) {

            throw new IllegalArgumentException(
                    "Cart item does not belong to this user");
        }

        Product product = cartItem.getProduct();

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock");
        }

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // ================= REMOVE CART ITEM =================

    public void removeFromCart(
            Long userId,
            Long cartItemId) {

        Cart cart = getCartByUser(userId);

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart item not found"));

        if (!cartItem.getCart().getId()
                .equals(cart.getId())) {

            throw new IllegalArgumentException(
                    "Cart item does not belong to this user");
        }

        cartItemRepository.delete(cartItem);
    }

    // ================= CLEAR CART =================

    public void clearCart(Long userId) {

        Cart cart = getCartByUser(userId);

        List<CartItem> items =
                cartItemRepository.findByCart(cart);

        cartItemRepository.deleteAll(items);
    }
}