package br.com.authentication.service;

import br.com.authentication.controller.request.PasswordRecoveryRequest;
import br.com.authentication.controller.request.PasswordResetRequest;
import br.com.authentication.domain.PasswordResetToken;
import br.com.authentication.domain.Users;
import br.com.authentication.repository.PasswordResetTokenRepository;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.exception.TokenExpiredException;
import br.com.authentication.service.exception.TokenNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordRecoveryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private MailService mailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordRecoveryService passwordRecoveryService;

    @Test
    void requestRecovery_WithExistingEmail_CreatesTokenAndSendsEmail() {
        PasswordRecoveryRequest request = new PasswordRecoveryRequest("user@email.com");
        Users user = new Users();
        user.setEmail("user@email.com");

        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));

        passwordRecoveryService.requestRecovery(request);

        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
        verify(mailService).sendRecoveryEmail(eq("user@email.com"), anyString());
    }

    @Test
    void requestRecovery_WithNonExistingEmail_DoesNothing() {
        PasswordRecoveryRequest request = new PasswordRecoveryRequest("naoexiste@email.com");

        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        passwordRecoveryService.requestRecovery(request);

        verifyNoInteractions(passwordResetTokenRepository, mailService);
    }

    @Test
    void resetPassword_WithValidToken_UpdatesPassword() {
        String token = UUID.randomUUID().toString();
        PasswordResetRequest request = new PasswordResetRequest(token, "novaSenha123");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUsed(false);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        Users user = new Users();
        user.setId(1L);
        resetToken.setUser(user);

        when(passwordResetTokenRepository.findByToken(UUID.fromString(token)))
                .thenReturn(Optional.of(resetToken));
        when(passwordEncoder.encode("novaSenha123")).thenReturn("new-encoded-hash");

        passwordRecoveryService.resetPassword(request);

        verify(passwordEncoder).encode("novaSenha123");
        verify(userRepository).save(user);
        assertTrue(resetToken.getUsed());
    }

    @Test
    void resetPassword_WithExpiredToken_ThrowsException() {
        String token = UUID.randomUUID().toString();
        PasswordResetRequest request = new PasswordResetRequest(token, "novaSenha123");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUsed(false);
        resetToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(passwordResetTokenRepository.findByToken(UUID.fromString(token)))
                .thenReturn(Optional.of(resetToken));

        assertThrows(TokenExpiredException.class,
                () -> passwordRecoveryService.resetPassword(request));
    }

    @Test
    void resetPassword_WithUsedToken_ThrowsException() {
        String token = UUID.randomUUID().toString();
        PasswordResetRequest request = new PasswordResetRequest(token, "novaSenha123");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUsed(true);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));

        when(passwordResetTokenRepository.findByToken(UUID.fromString(token)))
                .thenReturn(Optional.of(resetToken));

        assertThrows(TokenExpiredException.class,
                () -> passwordRecoveryService.resetPassword(request));
    }

    @Test
    void resetPassword_WithInvalidToken_ThrowsException() {
        String token = UUID.randomUUID().toString();
        PasswordResetRequest request = new PasswordResetRequest(token, "novaSenha123");

        when(passwordResetTokenRepository.findByToken(UUID.fromString(token)))
                .thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class,
                () -> passwordRecoveryService.resetPassword(request));
    }
}
