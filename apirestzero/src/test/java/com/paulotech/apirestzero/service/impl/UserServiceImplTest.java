package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import com.paulotech.apirestzero.service.UserService;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import com.paulotech.apirestzero.service.exceptions.EmailAlreadyExistsException;
import com.paulotech.apirestzero.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {
    private UserServiceImpl userServiceImpl;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder encoder;

    @Mock
    private UserService service;


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
        var command = new CreateUserCommand("Fulano", "fulano@12.com", "senha1234");

        when(this.userRepository.findByEmail(command.email())).thenReturn(Optional.of(User.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> service.createUser(command));

        verify(this.encoder, times(0)).encode(any());

        verify(this.userRepository, times(1)).findByEmail(command.email());

        verify(this.userRepository, times(0)).createUser(any());

        verify(this.userRepository, times(1)).createUser(any(User.class));

    }

    @Test
    void itShouldReturnUserDTOByID() {
        var uuid = UUID.randomUUID();

        var user = User.builder()
                .id(uuid)
                .name("Fulano")
                .email("XXXXXXXXXXXXX")
                .password("senha1234")
                .build();

        when(this.userRepository.findById(uuid)).thenReturn(Optional.of(user));

        var userDTO = this.service.getUserById(uuid);

        assertEquals(userDTO.id(), user.getId());
        assertEquals(userDTO.name(), user.getName());
        assertEquals(userDTO.email(), user.getEmail());
        assertEquals(userDTO.createdAt(), user.getCreatedAt());
        assertEquals(userDTO.updatedAt(), user.getUpdatedAt());

        verify(this.userRepository, times(1)).findById(uuid);
    }

    @Test
    void itShouldReturnAnExceptionWhenUserDoesnotExist() {
        var uuid = UUID.randomUUID();

        when(this.userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserById(uuid));

    }

    @Test
    void itShouldDeleteUserById() {
        var uuid = UUID.randomUUID();

        doNothing().when(this.userRepository).deleteUserByID(uuid);

        this.service.deleteUserById(uuid);

        verify(this.userRepository, times(1)).deleteUserByID(uuid);
    }
}
