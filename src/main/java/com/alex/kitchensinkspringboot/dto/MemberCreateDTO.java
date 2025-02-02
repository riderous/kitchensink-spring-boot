package com.alex.kitchensinkspringboot.dto;

import com.alex.kitchensinkspringboot.validation.UniqueEmail;
import jakarta.validation.constraints.*;

public record MemberCreateDTO(
        @Size(min = 1, max = 25)
        @Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
        String name,

        @NotEmpty
        @Email
        @UniqueEmail
        String email,

        @Size(min = 10, max = 12)
        @Digits(fraction = 0, integer = 12)
        String phoneNumber
) {
    public static MemberCreateDTO empty() {
        return new MemberCreateDTO("", "", "");
    }
}