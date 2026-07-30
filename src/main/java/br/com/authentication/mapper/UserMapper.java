package br.com.authentication.mapper;

import br.com.authentication.controller.request.UserRequest;
import br.com.authentication.controller.response.UserResponse;
import br.com.authentication.domain.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static Users toEntity(UserRequest request, String passwordHash) {

        Users user = new Users();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPasswordHash(passwordHash);
        return user;
    }

    public UserResponse toResponse(Users user) {

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
