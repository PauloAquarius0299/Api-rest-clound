package com.paulotech.apirestzero.repository.impl;

import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Repository
public class inMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList();

    public Optional<User> findByEmail(String email) {
        return this.users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public User createUser(User user) {
        this.users.add(user);
        return user;
    }

    public Optional<User> findById(UUID id) {
        return this.users.stream().filter(user -> id.equals(user.getId())).findAny();
    }

    public void deleteUserByID(UUID id) {
        this.users.removeIf(user -> id.equals(user.getId()));
    }

    public User save(User user) {
        this.users.removeIf(u -> user.getId().equals(u.getId()));
        this.users.add(user);
        return user;
    }
}
