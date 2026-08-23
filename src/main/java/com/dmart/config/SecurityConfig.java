package com.dmart.config;

import com.dmart.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ================= PASSWORD ENCODER =================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // ================= AUTHENTICATION MANAGER =================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // ================= CORS =================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of(
                "https://grocery-hub-ui.preview.emergentagent.com",
                "http://localhost:3000",
                "https://grocery-hub-ui.emergent.host"
        ));

        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // ================= SECURITY =================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // ================= AUTH =================

                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()


                        // ================= PRODUCTS =================

                        // Everyone can view products
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products/**"
                        ).permitAll()

                        // Only ADMIN and STAFF can create products
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        // Only ADMIN and STAFF can update products
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        // Only ADMIN and STAFF can partially update products
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/products/**"
                        ).hasAnyRole("ADMIN", "STAFF")

                        // Only ADMIN can delete products
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasRole("ADMIN")


                        // ================= ADMIN =================

                        // Only ADMIN can access admin APIs
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")


                        // ================= ORDERS =================

                        // Logged-in users
                        .requestMatchers(
                                "/api/orders/**"
                        ).authenticated()


                        // ================= CART =================

                        // Logged-in users
                        .requestMatchers(
                                "/api/cart/**"
                        ).authenticated()


                        // ================= CHECKOUT =================

                        // Logged-in users
                        .requestMatchers(
                                "/api/checkout/**"
                        ).authenticated()


                        // ================= OTHER APIs =================

                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}