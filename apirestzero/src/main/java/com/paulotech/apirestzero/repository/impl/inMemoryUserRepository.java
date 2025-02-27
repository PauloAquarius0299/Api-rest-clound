package com.paulotech.apirestzero.repository.impl;

import com.paulotech.apirestzero.domain.User;
import com.paulotech.apirestzero.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class inMemoryUserRepository implements UserRepository {
    private final List<User> users = new ArrayList();

    public Optional<User> findByEmail(String email) {
        return this.users.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public User createUser(User user) {
        this.users.add(user);
        return user;
    }
}
