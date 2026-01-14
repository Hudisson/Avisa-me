package com.hudissonxavier.avisame.service.users;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hudissonxavier.avisame.dto.users.LoginRequestDTO;
import com.hudissonxavier.avisame.dto.users.LoginResponseDTO;
import com.hudissonxavier.avisame.jwt.JwtService;
import com.hudissonxavier.avisame.model.agenda.TokenBlacklistModel;
import com.hudissonxavier.avisame.model.users.UserModel;
import com.hudissonxavier.avisame.repository.agenda.TokenBlacklistRepository;
import com.hudissonxavier.avisame.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável por gerenciar os processos de autenticação e sessão.
 * Coordena o login através da validação de credenciais e o logout via 
 * invalidação de tokens em blacklist.
 */

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenBlacklistRepository blacklistRepository;
    private final PasswordEncoder encoder;

    /**
     * Realiza a autenticação do usuário.
     * Verifica a existência do e-mail e a integridade da senha usando o PasswordEncoder.
     * @param dto Contém e-mail e senha informados pelo cliente.
     * @return LoginResponseDTO contendo o token JWT gerado.
     * @throws ResponseStatusException 401 caso o e-mail não exista ou a senha seja incorreta.
     */
    public LoginResponseDTO login(LoginRequestDTO dto) {

        // Localiza o usuário ou interrompe com erro de autorização
        UserModel user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        // Compara o hash da senha enviada com o hash salvo no banco
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        // Emite a credencial de acesso (Token)
        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getName());
        return new LoginResponseDTO(token);
    }

    /**
     * Invalida a sessão atual do usuário (Logout).
     * Persiste o token atual na lista negra para impedir seu uso futuro.
     * @param token O token JWT que deve ser revogado.
     */
    public void logout(String token) {
        blacklistRepository.save(TokenBlacklistModel.builder().token(token).build());
    }
}
