package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    public String authenticate(String username, String password) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        var authentication = authenticationManager.authenticate(authenticationToken);
        return this.jwtService.generateToken((UserDetails) authentication.getPrincipal());
    }
}
