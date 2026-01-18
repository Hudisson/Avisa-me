package com.hudissonxavier.avisame.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * Serviço responsável pela geração, decodificação e validação de tokens JWT.
 * Utiliza a biblioteca JJWT para manipular os tokens e garantir a integridade
 * das informações através de assinaturas HMAC-SHA.
 */

@Service
public class JwtService {
    
    // Chave secreta injetada a partir das variáveis de ambiente (application.properties)
    @Value("${api.security.token.secret}")
    private String secret;

    private SecretKey key;

    /**
     * Inicializa a chave de assinatura após a injeção do valor secreto.
     * Converte a String secreta em um objeto SecretKey compatível com HMAC-SHA.
     */
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Gera um novo token JWT contendo informações básicas do usuário.
     * @param userId Identificador único do usuário.
     * @param email E-mail (utilizado como Subject principal).
     * @param name Nome para exibição rápida no frontend.
     * @return String contendo o token JWT assinado e compacto.
     */
    public String generateToken(UUID userId, String email, String name) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(email)                                       // Identificador principal do token
                .claim("id", userId.toString())                 // Dados extras (Payload claims) adiciona id
                .claim("name", name)                            // adiciona nome
                .issuedAt(Date.from(now))                             // Data de criação
                .expiration(Date.from(now.plus(1, ChronoUnit.DAYS))) // Validade de 24h
                .signWith(key)                                      // Assinatura digital para evitar fraudes
                .compact();
    }

    // Extrai o e-mail (subject) do payload do token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Extrai o ID do usuário de dentro das claims do token
    public UUID extractUserId(String token) {
        String id = extractClaims(token).get("id", String.class);
        return UUID.fromString(id);
    }

    // Extrai o nome do usuário de dentro das claims do token
    public String extractName(String token) {
        return extractClaims(token).get("name", String.class);
    }

    /**
     * Valida se o token é matematicamente íntegro e se não está expirado.
     * @return true se o token for confiável, false caso contrário.
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Método interno para descriptografar e extrair o corpo (Claims) do token.
     * Lança exceção automaticamente se a assinatura for inválida ou o tempo expirou.
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
