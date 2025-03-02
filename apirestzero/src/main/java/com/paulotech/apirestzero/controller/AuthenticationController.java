package com.paulotech.apirestzero.controller;

import com.paulotech.apirestzero.controller.dto.AuthenticationUserRequestDTO;
import com.paulotech.apirestzero.controller.dto.AuthenticationUserResponseDTO;
import com.paulotech.apirestzero.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationUserResponseDTO> authenticate(@RequestBody @Valid AuthenticationUserRequestDTO authenticationUserRequestDTO){
        String accessToken = authenticationService.authenticate(
                authenticationUserRequestDTO.username(),
                authenticationUserRequestDTO.password()
        );

        var response = new AuthenticationUserResponseDTO(accessToken);
        return ResponseEntity.ok(response);
    }
}
