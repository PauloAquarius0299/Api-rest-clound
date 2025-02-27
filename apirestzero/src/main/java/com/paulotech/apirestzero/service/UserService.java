package com.paulotech.apirestzero.service;

import com.paulotech.apirestzero.service.dto.CreateUserCommand;

import java.util.UUID;

public interface UserService {
    UUID createUser(CreateUserCommand command);
}
