package com.paulotech.apirestzero.service;

import com.paulotech.apirestzero.controller.dto.UserDTO;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;

import java.util.UUID;

public interface UserService {
    UUID createUser(CreateUserCommand command);
    UserDTO getUserById(UUID id);
    void deleteUserById(UUID id);
}
