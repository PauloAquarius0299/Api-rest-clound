package com.paulotech.apirestzero.service.impl;

import com.paulotech.apirestzero.controller.dto.UserDTO;
import com.paulotech.apirestzero.repository.UserRepository;
import com.paulotech.apirestzero.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    @Value("${security.jwt.secret-key:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTYzMjMwNzIyMiwiZXhwIjoxNjMyMzkzNjIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c}")
    private String secretKey;

    @Value("${security.jwt.expiration:36000000}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try{
            return this.getClaims(token)
                    .getExpiration()
                    .after(new Date());
        }catch (ExpiredJwtException e){
            return false;
        }
    }

    public UserDTO getUserFromToken(String token) {
        var claims = this.getClaims(token);
        var email = claims.getSubject();
        return this.userRepository.findByEmail(token)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ))
                .orElseThrow();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }
}