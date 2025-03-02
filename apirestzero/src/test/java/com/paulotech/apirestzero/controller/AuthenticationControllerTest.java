package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.AuthenticationUserRequestDTO;
import com.paulotech.apirestzero.service.AuthenticationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationControllerTest {
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService service;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationController = new AuthenticationController(this.service);
    }

    @BeforeEach
    void setUpEach() {
        Mockito.reset(this.service);
    }

    @Test
    void itShouldReturnAccessToken() {
        var request = new AuthenticationUserRequestDTO(
                "fulane@gmail.com",
                "senha1234"
        );

        when(this.service.authenticate(request.username(), request.password())).thenReturn("accessToken");

        var response = this.authenticationController.authenticate(request);
        var body = response.getBody();

        assertEquals(body.accessToken(), "accessToken");
        verify(this.service).authenticate(request.username(), request.password());
    }
}
