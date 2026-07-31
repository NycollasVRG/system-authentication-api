package br.com.authentication.controller;

import br.com.authentication.controller.request.PasswordRecoveryRequest;
import br.com.authentication.controller.request.PasswordResetRequest;
import br.com.authentication.controller.response.MessageResponse;
import br.com.authentication.service.PasswordRecoveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/recuperar-senha")
    public ResponseEntity<MessageResponse> requestRecovery(@Valid @RequestBody PasswordRecoveryRequest request) {
        passwordRecoveryService.requestRecovery(request);
        return ResponseEntity.ok(new MessageResponse("Se o e-mail estiver cadastrado, enviaremos instruções"));
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        passwordRecoveryService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Senha redefinida com sucesso"));
    }
}
