package com.paulotech.apirestzero.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AutheticateServiceImplTest {

    @Mock
    private AuthenticateServiceImpl service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        this.service = new AuthenticateServiceImpl(
                this.authenticationManager,
                this.jwtService
        );
    }

    @BeforeEach
    void resetMocks(){
        Mockito.reset(this.authenticationManager);
        Mockito.reset(this.jwtService);
    }

    @Test
    void itShouldAuthenticateUserAndGenerateToken() {
        var username = "fulano12";
        var password = "123456";

        when(this.authenticationManager.authenticate(any())).thenReturn(authentication);
        when(this.jwtService.generateToken(any())).thenReturn("accessToken");

        var result = this.service.authenticate(username, password);

        assertEquals("accessToken", result);
        verify(this.authenticationManager, Mockito.times(1)).authenticate(any());
        verify(this.jwtService, times(0)).generateToken(userDetails);
    }
}
