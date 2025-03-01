package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.controller.dto.UserDTO;
import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import com.paulotech.apirestzero.service.UserService;
import com.paulotech.apirestzero.service.dto.CreateUserCommand;
import com.paulotech.apirestzero.service.exceptions.EmailAlreadyExistsException;
import com.paulotech.apirestzero.service.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UUID createUser(CreateUserCommand command) {

        var userOptional = userRepository.findByEmail(command.email());

        if (userOptional.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        var passwordEncoded = encoder.encode(command.password());

        var user = User.builder()
                .name(command.name())
                .email(command.email())
                .password(passwordEncoded)
                .build();
        this.userRepository.createUser(user);
        return user.getId();
    }

    @Override
    public UserDTO getUserById(UUID id) {
        var user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public void deleteUserById(UUID id) {
        this.userRepository.deleteUserByID(id);
    }
}
