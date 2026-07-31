package br.com.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendRecoveryEmail(String to, String token) {
        log.info("Token de recuperação para {}: {}", to, token);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Recuperação de Senha");
            message.setText("Seu token de recuperação de senha é: " + token);

            mailSender.send(message);
        } catch (Exception exception) {
            log.warn("Não foi possível enviar o e-mail real. O token foi registrado no log.");
        }
    }
}
