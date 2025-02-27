package com.paulotech.apirestzero.service.dto;

public record CreateUserCommand(
        String name,
        String email,
        String password
) {
}
