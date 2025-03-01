package com.paulotech.apirestzero.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class User {
    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
