package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.*;
import com.paulotech.apirestzero.service.UserService;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<GetUserByIDRequestDTO> getUserById(
            @PathVariable UUID id
    ){
        var user = this.userService.getUserById(id);
        var response = new GetUserByIDRequestDTO(
                user.id(),
                user.name(),
                user.email(),
                user.createdAt().toString(),
                user.updatedAt().toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserByIDRequestDTO> getAuthenticatedUser(){
        var userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var response = new GetUserByIDRequestDTO(
                userDTO.id(),
                userDTO.name(),
                userDTO.email(),
                userDTO.createdAt().toString(),
                userDTO.updatedAt().toString()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserByID(
            @PathVariable UUID id
    ){
        this.userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID id,
                                           @Valid @RequestBody UpdateUserRequestDTO updateUserRequest){
        this.userService.updateUserPassword(id, updateUserRequest.password());
        return ResponseEntity.noContent().build();
    }
}
