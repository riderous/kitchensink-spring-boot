package com.alex.kitchensinkspringboot.validation;

import com.alex.kitchensinkspringboot.repository.MemberRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final MemberRepository memberRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true; // ✅ Allow blank emails (handled by @NotBlank separately)
        }
        return memberRepository.findByEmail(email).isEmpty(); // ✅ Return false if email exists
    }
}