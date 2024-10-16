package com.ms.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserRecordDto(@NotBlank String name, @NotBlank @Email String email, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
}