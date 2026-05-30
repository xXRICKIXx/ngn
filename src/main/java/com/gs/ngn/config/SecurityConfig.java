package com.gs.ngn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * RN07 – Usuários comuns não poderão alterar parâmetros críticos.
 * ROLE_ADMIN: acesso total (inclui PATCH atmosphere, DELETE, activate/deactivate).
 * ROLE_OPERATOR: leitura e registro de métricas.
 * ROLE_VIEWER: somente leitura.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // H2 console
            .authorizeHttpRequests(auth -> auth
                // H2 Console e Swagger livres
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/api-docs/**").permitAll()
                // Leitura livre
                .requestMatchers(HttpMethod.GET, "/api/v1/**").hasAnyRole("VIEWER", "OPERATOR", "ADMIN")
                // Operações críticas: apenas ADMIN (RN07)
                .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/habitats/*/atmosphere").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/modules/*/activate").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/modules/*/deactivate").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/alerts/*/trigger-response").hasRole("ADMIN")
                // Demais POST/PUT/PATCH: OPERATOR ou ADMIN
                .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAnyRole("OPERATOR", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("OPERATOR", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/**").hasAnyRole("OPERATOR", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(org.springframework.security.config.Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin").password(encoder.encode("admin123")).roles("ADMIN").build(),
            User.withUsername("operator").password(encoder.encode("op123")).roles("OPERATOR").build(),
            User.withUsername("viewer").password(encoder.encode("view123")).roles("VIEWER").build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
