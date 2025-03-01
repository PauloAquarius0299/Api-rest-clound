package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import com.paulotech.apirestzero.service.exceptions.EmailAlreadyExistsException;
import com.paulotech.apirestzero.service.exceptions.UserNotFoundException;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(userRepository, encoder);
    }

    @Test
    void itShouldCreateTheUserIfEmailIsAvailable() {
        var command = new CreateUserCommand("Fulano", "fulano@12.com", "senha1234");

        when(encoder.encode(command.password())).thenReturn("senha1234");
        when(userRepository.findByEmail(command.email())).thenReturn(Optional.empty());
        when(userRepository.createUser(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, User.class));

        var userID = userServiceImpl.createUser(command);

        assertNotNull(userID);
        verify(encoder, times(1)).encode(command.password());
        verify(userRepository, times(1)).findByEmail(command.email());

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).createUser(captor.capture());

        var createdUser = captor.getValue();
        assertEquals("senha1234", createdUser.getPassword());
        assertEquals(userID, createdUser.getId());
    }

    @Test
    void itShouldThrowAnExceptionIfEmailIsNotAvailable() {
        var command = new CreateUserCommand("Fulano", "fulano@12.com", "senha1234");

        when(userRepository.findByEmail(command.email())).thenReturn(Optional.of(User.builder().build()));

        assertThrows(EmailAlreadyExistsException.class, () -> userServiceImpl.createUser(command));

        verify(encoder, times(0)).encode(any());
        verify(userRepository, times(1)).findByEmail(command.email());
        verify(userRepository, times(0)).createUser(any());
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

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        var userDTO = userServiceImpl.getUserById(uuid);

        assertEquals(user.getId(), userDTO.id());
        assertEquals(user.getName(), userDTO.name());
        assertEquals(user.getEmail(), userDTO.email());
        assertEquals(user.getCreatedAt(), userDTO.createdAt());
        assertEquals(user.getUpdatedAt(), userDTO.updatedAt());

        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void itShouldReturnAnExceptionWhenUserDoesNotExist() {
        var uuid = UUID.randomUUID();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(uuid));

        verify(userRepository, times(1)).findById(uuid);
    }

    @Test
    void itShouldDeleteUserById() {
        var uuid = UUID.randomUUID();

        doNothing().when(userRepository).deleteUserByID(uuid);

        userServiceImpl.deleteUserById(uuid);

        verify(userRepository, times(1)).deleteUserByID(uuid);
    }

    @Test
    void itShouldUpdateUserById(){
        var uuid = UUID.randomUUID();
        var password = "senha1234";

        var user = User.builder()
                .id(uuid)
                .name("Fulano")
                .email("fulano@12.com")
                .password("senha1234")
                .build();

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(encoder.encode(password)).thenReturn("senha1234");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, User.class));

        userServiceImpl.updateUser(uuid, password);

        verify(userRepository, times(1)).findById(uuid);
        verify(encoder, times(1)).encode(password);

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());

        var updatedUser = captor.getValue();
        assertEquals(updatedUser.getPassword(), "senha1234");
    }

    @Test
    void itShouldThrowAnErrorIfUserDoesNotExistWhenUpdating(){
        var uuid = UUID.randomUUID();
        var password = "senha1234";

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceImpl.updateUser(uuid, password));

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(0)).save(any());
    }
}
