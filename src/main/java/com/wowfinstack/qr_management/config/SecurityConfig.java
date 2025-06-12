package com.wowfinstack.qr_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/qrcode/generate").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/configuration/ui",
                                "/swagger-ui/**",
                                "/configuration/security",
                                "/webjars/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}