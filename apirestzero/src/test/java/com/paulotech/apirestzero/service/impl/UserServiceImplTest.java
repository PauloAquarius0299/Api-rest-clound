package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder encoder;

    @BeforeAll
    void setUp(){
        MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(userRepository, encoder);
    }

    @BeforeEach
    void setUpEach(){
        reset(this.encoder, this.userRepository);
    }

    @Test
    void itShouldCreateTheUserIfEmailIsAvailable(){

        var command = new CreateUserCommand("Fulano", "fulano@12.com", "senha1234");

        when(this.encoder.encode(command.password())).thenReturn("senha1234");

        when(this.userRepository.findByEmail(command.email())).thenReturn(Optional.empty());

        when(this.userRepository.createUser(any(User.class)))
                .thenAnswer(invocation -> {
                    var user = invocation.getArgument(0, User.class);
                    return user;
                });

        var userID = userServiceImpl.createUser(command);

        assertNotNull(userID);
        verify(this.encoder, times(1)).encode(command.password());
        verify(this.userRepository, times(1)).findByEmail(command.email());

        var captor = ArgumentCaptor.forClass(User.class);

        verify(this.userRepository, times(1)).createUser(captor.capture());

        verify(this.userRepository, times(1)).createUser(any(User.class));

        var createdUser = captor.getValue();
        assertEquals(createdUser.getPassword(), "encoderPassword");
        assertEquals(createdUser.getId(), userID);

    }

    @Test
    void itShouldThrowAnExceptionIfEmailIsNotAvailable(){

    }
}
