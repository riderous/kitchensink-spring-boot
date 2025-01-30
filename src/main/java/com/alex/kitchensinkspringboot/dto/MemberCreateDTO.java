package com.alex.kitchensinkspringboot.dto;

import jakarta.validation.constraints.*;

public record MemberCreateDTO(
        @NotBlank
        @Size(min = 1, max = 25)
        @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 10, max = 12)
        @Pattern(regexp = "\\d+", message = "Phone number must contain only digits")
        String phoneNumber
) {
}