package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.CreateUserRequestDTO;
import com.paulotech.apirestzero.controller.dto.CreateUserResponseDTO;
import com.paulotech.apirestzero.service.UserService;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @Valid @RequestBody CreateUserRequestDTO createUserRequestDTO
    ) {
        var command = new CreateUserCommand(
                createUserRequestDTO.name(),
                createUserRequestDTO.email(),
                createUserRequestDTO.password()
        );

        var userID = this.userService.createUser(command);

        var response = new CreateUserResponseDTO(userID);
        return ResponseEntity.ok(response);
    }
}
