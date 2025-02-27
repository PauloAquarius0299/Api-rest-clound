package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.CreateUserRequestDTO;
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
}
