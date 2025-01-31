package com.alex.kitchensinkspringboot.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class) // ✅ Links to the validator class
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {

    String message() default "Email taken"; // ✅ Default error message

    Class<?>[] groups() default {}; // ✅ Needed for validation grouping

    Class<? extends Payload>[] payload() default {}; // ✅ Allows metadata on constraints
}