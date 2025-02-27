package com.paulotech.apirestzero.repository;

import com.paulotech.apirestzero.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User createUser(User user);
}
