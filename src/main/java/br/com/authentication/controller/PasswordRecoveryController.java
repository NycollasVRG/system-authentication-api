package br.com.authentication.controller;

import br.com.authentication.controller.request.PasswordRecoveryRequest;
import br.com.authentication.controller.request.PasswordResetRequest;
import br.com.authentication.controller.response.MessageResponse;
import br.com.authentication.service.PasswordRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Recuperação de Senha", description = "Endpoints de recuperação de senha")
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping("/recuperar-senha")
    @Operation(summary = "Solicitar recuperação", description = "Envia um token de recuperação para o e-mail informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instruções enviadas (se o e-mail existir)"),
            @ApiResponse(responseCode = "400", description = "E-mail inválido")
    })
    public ResponseEntity<MessageResponse> requestRecovery(@Valid @RequestBody PasswordRecoveryRequest request) {
        passwordRecoveryService.requestRecovery(request);
        return ResponseEntity.ok(new MessageResponse("Se o e-mail estiver cadastrado, enviaremos instruções"));
    }

    @PostMapping("/resetar-senha")
    @Operation(summary = "Redefinir senha", description = "Redefine a senha utilizando um token válido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token expirado, já utilizado ou dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Token não encontrado")
    })
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        passwordRecoveryService.resetPassword(request);
        return ResponseEntity.ok(new MessageResponse("Senha redefinida com sucesso"));
    }
}
