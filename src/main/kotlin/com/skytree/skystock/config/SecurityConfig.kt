package com.skytree.skystock.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    // 공개 — 인증 불필요
                    .requestMatchers("/api/auth/**").permitAll()

                    // Supplier 관리 — ADMIN만 CUD
                    .requestMatchers(HttpMethod.POST, "/api/suppliers/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PATCH, "/api/suppliers/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/suppliers/**").hasRole("ADMIN")

                    // PurchaseOrder 관리 — WAREHOUSE_MANAGER, ADMIN만 CUD
                    .requestMatchers(HttpMethod.POST, "/api/purchase-orders/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")
                    .requestMatchers(HttpMethod.PATCH, "/api/purchase-orders/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                    // StockAlert 관리 — WAREHOUSE_MANAGER, ADMIN만 CUD; DELETE는 ADMIN만
                    .requestMatchers(HttpMethod.DELETE, "/api/stock-alerts/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/stock-alerts/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")
                    .requestMatchers(HttpMethod.PATCH, "/api/stock-alerts/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                    // Stats — supplier performance는 WAREHOUSE_MANAGER+
                    .requestMatchers("/api/stats/supplier-performance/**").hasAnyRole("ADMIN", "WAREHOUSE_MANAGER")

                    // 나머지 — 인증된 사용자 (VIEWER 포함)
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
