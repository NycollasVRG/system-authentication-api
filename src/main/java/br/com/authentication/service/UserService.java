package br.com.authentication.service;

import br.com.authentication.controller.request.UserRequest;
import br.com.authentication.controller.response.UserResponse;
import br.com.authentication.domain.Users;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import br.com.authentication.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(UserRequest request) {

        userValidator.validateNewUser(request);

        String hash = passwordEncoder.encode(request.password());

        Users userToSave = UserMapper.toEntity(request, hash);

        Users savedUser = userRepository.save(userToSave);

        return userMapper.toResponse(savedUser);
    }
}
