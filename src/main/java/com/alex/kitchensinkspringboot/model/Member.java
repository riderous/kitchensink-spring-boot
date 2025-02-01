package com.alex.kitchensinkspringboot.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "members") // ✅ Collection name in MongoDB
public class Member {

    @Id
    private String id;

    @NotBlank
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[^0-9]*")
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 12)
    @Pattern(regexp = "\\d+")
    private String phoneNumber;
}