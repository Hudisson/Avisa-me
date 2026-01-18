package com.hudissonxavier.avisame.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hudissonxavier.avisame.repository.agenda.TokenBlacklistRepository;
import com.hudissonxavier.avisame.repository.users.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de Autenticação JWT.
 * Intercepta todas as requisições HTTP uma única vez (OncePerRequestFilter) 
 * para validar a presença e a integridade do token Bearer no cabeçalho Authorization.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenBlacklistRepository blacklistRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Extrai o cabeçalho de autorização
        String authHeader = request.getHeader("Authorization");

        // Verifica se o formato é "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validação de Segurança: Verifica se o token foi invalidado via logout (Blacklist)
            if (!blacklistRepository.existsByToken(token)) {
                try {
                    // Decodifica o e-mail (subject) de dentro do token
                    String email = jwtService.extractEmail(token);

                    // Busca o usuário no banco e autentica no contexto do Spring Security
                    userRepository.findByEmail(email).ifPresent(user -> {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        null,
                                        //Collections.emptyList(), // Local para definir Roles futuramente
                                        user.getAuthorities()
                                );

                        // Define o usuário como "autenticado" para esta requisição específica
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    });
                    
                } catch (Exception e) {
                    // // Se o token for inválido ou expirado, garante que o contexto esteja limpo
                    SecurityContextHolder.clearContext();
                }
            }
        }

        // Segue para o próximo filtro na corrente (ou para o Controller)
        filterChain.doFilter(request, response);
    }
}
