package com.paulotech.apirestzero.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationUserResponseDTO(
        @Schema(
                description = "Access token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTYzMjMwNzIyMiwiZXhwIjoxNjMyMzkzNjIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        )
        String accessToken
) {
}
