package br.com.authentication.service.validator;

import br.com.authentication.controller.request.UserRequest;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.exception.DuplicateEmailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validateNewUser_WithExistingEmail_ThrowsException() {
        UserRequest request = new UserRequest("Nome", "existente@email.com", "senha123");

        when(userRepository.existsByEmail("existente@email.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class,
                () -> userValidator.validateNewUser(request));
    }

    @Test
    void validateNewUser_WithNewEmail_DoesNotThrow() {
        UserRequest request = new UserRequest("Nome", "novo@email.com", "senha123");

        when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);

        assertDoesNotThrow(() -> userValidator.validateNewUser(request));
    }
}
