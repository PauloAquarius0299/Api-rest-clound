package com.paulotech.apirestzero.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRequestDTO(
       @Schema(description = "Nova senha", example = "senha123")
       @NotEmpty
       String password
) {
}
