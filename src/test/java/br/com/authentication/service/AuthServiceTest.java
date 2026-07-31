package br.com.authentication.service;

import br.com.authentication.controller.request.AuthRequest;
import br.com.authentication.domain.Users;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.exception.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "expiration", 3600L);
    }

    @Test
    void authenticate_WithValidCredentials_ReturnsToken() {
        AuthRequest request = new AuthRequest("user@email.com", "senha123");
        Users user = new Users();
        user.setId(1L);
        user.setEmail("user@email.com");
        user.setPasswordHash("encoded-hash");
        user.setEnabled(true);

        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", "encoded-hash")).thenReturn(true);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(
                new Jwt("jwt-token", Instant.now(), Instant.now().plusSeconds(3600),
                        Map.of("alg", "RS256"), Map.of("sub", "1")));

        var response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
    }

    @Test
    void authenticate_WithInvalidEmail_ThrowsException() {
        AuthRequest request = new AuthRequest("naoexiste@email.com", "senha123");

        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.authenticate(request));
    }

    @Test
    void authenticate_WithWrongPassword_ThrowsException() {
        AuthRequest request = new AuthRequest("user@email.com", "senha-errada");
        Users user = new Users();
        user.setEmail("user@email.com");
        user.setPasswordHash("encoded-hash");
        user.setEnabled(true);

        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha-errada", "encoded-hash")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.authenticate(request));
    }

    @Test
    void authenticate_WithDisabledUser_ThrowsException() {
        AuthRequest request = new AuthRequest("user@email.com", "senha123");
        Users user = new Users();
        user.setEmail("user@email.com");
        user.setPasswordHash("encoded-hash");
        user.setEnabled(false);

        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", "encoded-hash")).thenReturn(true);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.authenticate(request));
    }
}
