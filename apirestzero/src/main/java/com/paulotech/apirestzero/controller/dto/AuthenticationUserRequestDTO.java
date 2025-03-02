package com.paulotech.apirestzero.controller.dto;

public record AuthenticationUserRequestDTO(
        String username,
        String password
) {
}
