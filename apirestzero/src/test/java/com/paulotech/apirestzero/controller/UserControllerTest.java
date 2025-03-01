package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.CreateUserRequestDTO;
import com.paulotech.apirestzero.controller.dto.UpdateUserRequestDTO;
import com.paulotech.apirestzero.controller.dto.UserDTO;
import com.paulotech.apirestzero.service.UserService;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    private UserController userController;

    @Mock private UserService userService;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userController = new UserController(this.userService);
    }

    @BeforeEach
    void setUpEach() {
        Mockito.reset(this.userService);
    }

    @Test
    void itShouldCallServiceAndReturnCreatedUserID() {
        var request = new CreateUserRequestDTO(
                "fulano",
                "fulano@12.com",
                "senha12345"
        );

        UUID expectedUserId = UUID.randomUUID();
        when(this.userService.createUser(any(CreateUserCommand.class)))
                .thenReturn(expectedUserId);

        var response = this.userController.createUser(request);
        var body = response.getBody();

        assertNotNull(body);
        assertEquals(expectedUserId, body.id());

        var captor = ArgumentCaptor.forClass(CreateUserCommand.class);
        verify(this.userService, times(1)).createUser(captor.capture());

        var command = captor.getValue();
        assertEquals(request.email(), command.email());
        assertEquals(request.name(), command.name());
        assertEquals(request.password(), command.password());
    }

    @Test
    void itShouldReturnUserByID() {
        var uuid = UUID.randomUUID();

        var userDTO = new UserDTO(
                uuid,
                "fulano",
                "fulano@12.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(this.userService.getUserById(uuid))
                .thenReturn(userDTO);

        var response = this.userController.getUserById(uuid);
        var body = response.getBody();

        assertEquals(body.id(), userDTO.id());
        assertEquals(body.name(), userDTO.name());
        assertEquals(body.email(), userDTO.email());
        assertEquals(body.createdAt(), userDTO.createdAt().toString());
        assertEquals(body.updatedAt(), userDTO.updatedAt().toString());

        verify(this.userService, times(1))
                .getUserById(uuid);
    }

    @Test
    void itShouldDeleteUserByID() {
        var uuid = UUID.randomUUID();

        this.userController.deleteUserByID(uuid);

        verify(this.userService, times(1))
                .deleteUserById(uuid);
    }

    @Test
    void itShouldUpdateUserByID(){
        var uuid = UUID.randomUUID();
        var request = new UpdateUserRequestDTO("senha12345");

        this.userController.updateUser(uuid, request);

        verify(this.userService, times(1))
                .updateUserPassword(uuid, "senha12345");
    }
}
