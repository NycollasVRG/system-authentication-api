package br.com.authentication.service;

import br.com.authentication.controller.request.PasswordRecoveryRequest;
import br.com.authentication.controller.request.PasswordResetRequest;
import br.com.authentication.domain.PasswordResetToken;
import br.com.authentication.domain.Users;
import br.com.authentication.repository.PasswordResetTokenRepository;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.exception.TokenExpiredException;
import br.com.authentication.service.exception.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordRecoveryService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void requestRecovery(PasswordRecoveryRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(user -> {
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(UUID.randomUUID());
            resetToken.setUser(user);
            resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

            passwordResetTokenRepository.save(resetToken);

            mailService.sendRecoveryEmail(user.getEmail(), resetToken.getToken().toString());
        });
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        UUID tokenUuid = UUID.fromString(request.token());

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(tokenUuid)
                .orElseThrow(TokenNotFoundException::new);

        if (resetToken.getUsed()) {
            throw new TokenExpiredException("Token já utilizado");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expirado");
        }

        Users user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        resetToken.setUsed(true);
    }
}
