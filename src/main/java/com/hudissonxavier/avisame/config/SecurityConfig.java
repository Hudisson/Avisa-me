package com.hudissonxavier.avisame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hudissonxavier.avisame.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aponta para a config explícita
        .csrf(csrf -> csrf.disable()) 

        .sessionManagement(session -> session
            .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
            // .requestMatchers("/auth/login", "/auth/register").permitAll()
            .requestMatchers("/auth/login", "/auth/login/**", "/auth/register", "/auth/register/**").permitAll()
            
            .requestMatchers("/users/**").authenticated()
            .requestMatchers("/tasks/**").authenticated()
            .requestMatchers("/events/**").authenticated()
            .requestMatchers("/config", "/config/**").authenticated()
            
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define as regras globais de CORS da API.
     * Permite que o Angular (porta 4200) envie requisições com cabeçalhos e métodos HTTP.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permite a origem do seu projeto Angular
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); 
        
        // Permite os principais métodos HTTP usados em REST
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Permite cabeçalhos padrões (como Authorization para carregar o Token JWT)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control"));
        
        // Permite que cookies ou credenciais HTTP sejam compartilhados (caso precise no futuro)
        configuration.setAllowCredentials(true);

        // Aplica essa configuração para todas as rotas da API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}