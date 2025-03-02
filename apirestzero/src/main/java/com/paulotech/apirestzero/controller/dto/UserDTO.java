package com.paulotech.apirestzero.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
