package com.paulotech.apirestzero.controller.dto;

import java.util.UUID;

public record GetUserByIDRequestDTO(
        UUID id,
        String name,
        String email,
        String createdAt,
        String updatedAt
) {
}
