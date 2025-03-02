package com.paulotech.apirestzero.service;

import com.paulotech.apirestzero.controller.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token);
    UserDTO getUserFromToken(String token);
}
