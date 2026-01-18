package com.hudissonxavier.avisame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

        http.csrf(csrf -> csrf.disable()) // Desabilita CSRF, pois a autenticação via Token é stateless

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
