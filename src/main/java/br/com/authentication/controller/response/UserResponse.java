package br.com.authentication.controller.response;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,
        String name,
        String email,
        Boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
