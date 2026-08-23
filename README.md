# 🛒 Mini D-Mart Backend

REST API backend for **Mini D-Mart**, a full-stack online grocery shopping application.

The backend is developed using **Java Spring Boot** and provides APIs for user authentication, product browsing, cart management, checkout, and order management. The application uses **PostgreSQL** for persistent data storage and **JWT** for authentication and authorization.

---

## 🌐 Live Application

### Frontend
https://grocery-hub-ui.emergent.host

### Backend API
https://mini-demart-backend-4.onrender.com

---

## 📌 Project Overview

Mini D-Mart is an online grocery shopping platform where users can browse grocery products, create an account, log in securely, add products to their cart, and place orders.

The backend provides RESTful APIs that connect the frontend application with the PostgreSQL database.

### Main Responsibilities

- User registration and login
- JWT-based authentication
- Password encryption
- Product management
- Product category management
- Shopping cart management
- Order management
- Checkout functionality
- Role-based authorization
- PostgreSQL database integration
- CORS configuration
- REST API development

---

## 🚀 Features

### 👤 User Authentication

- User registration
- User login
- Password encryption using BCrypt
- JWT token-based authentication
- Protected API endpoints
- Role-based authorization

### 🛍️ Product Management

- Retrieve all products
- Retrieve products by category
- Retrieve individual product details
- Product stock management
- Product images
- Product descriptions
- Product pricing
- Product category information

### 🛒 Shopping Cart

- Add products to cart
- View cart items
- Update cart quantity
- Remove products from cart

### 📦 Orders

- Create orders
- View user orders
- Manage order information
- Order items and quantities

### 💳 Checkout

- Process checkout requests
- Create orders from cart items
- Manage checkout-related operations

### 🔐 Security

- Spring Security
- JWT authentication
- BCrypt password hashing
- Stateless session management
- Role-based access control

---

# 🛠️ Tech Stack

## Backend

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Security**
- **Spring Data JPA**
- **Hibernate**
- **JWT**
- **Maven**

## Database

- **PostgreSQL**

## Deployment

- **Render**

## API

- **REST API**
- **JSON**

---

# 🏗️ Backend Architecture

The application follows a layered architecture.

```text
Client / Frontend
       │
       ▼
 REST Controllers
       │
       ▼
    Services
       │
       ▼
  Repositories
       │
       ▼
   Hibernate / JPA
       │
       ▼
   PostgreSQL
