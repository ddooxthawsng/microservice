package com.thangdm.auth_service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid Date Of Birth";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int min();
}
