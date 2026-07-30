package br.com.authentication.service.validator;

import br.com.authentication.controller.request.UserRequest;
import br.com.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateNewUser(UserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }
    }
}
