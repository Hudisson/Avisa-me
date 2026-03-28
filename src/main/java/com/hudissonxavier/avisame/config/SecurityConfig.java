package com.hudissonxavier.avisame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

import com.hudissonxavier.avisame.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

/**
 * Classe de configuração central do Spring Security.
 * Define as políticas de autenticação, autorização e proteção da API, 
 * além de configurar os filtros de segurança personalizados.
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    // Filtro personalizado que intercepta requisições para validar o token JWT
    private final JwtAuthFilter jwtAuthFilter;

    /**
     * Define a corrente de filtros de segurança (Security Filter Chain).
     * Configura as permissões de acesso por rota e desabilita proteções 
     * não necessárias para APIs REST (como CSRF).
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable()) // Desabilita CSRF, pois a autenticação via Token é stateless
       
        .sessionManagement(session -> session
            .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
        )
            .authorizeHttpRequests(auth -> auth

                /** * Define as rotas públicas (Whitelisting).
                 * Login e Registro devem ser acessíveis sem token.
                 */
                .requestMatchers("/auth/login", "/auth/register").permitAll()
                
                //Protege todas as rotas de usuários e qualquer outra requisição
                .requestMatchers("/users/**").authenticated()
                .requestMatchers("/tasks/**").authenticated()
                .requestMatchers("/events/**").authenticated()
                .requestMatchers("/config", "/config/**").authenticated()
                
                // Demais rotas protegidas
                .anyRequest().authenticated()
            )

            /** * Adiciona o filtro JWT antes do filtro de autenticação padrão do Spring.
             * Isso garante que, se houver um token, ele seja validado antes de qualquer outra coisa.
             */
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite todas as origens, métodos e headers
        configuration.setAllowedOrigins(Arrays.asList("*")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Define o algoritmo de hash para senhas.
     * O BCrypt é um dos padrões mais seguros, aplicando "salting" automaticamente
     * para proteger contra ataques de dicionário e tabelas arco-íris.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
