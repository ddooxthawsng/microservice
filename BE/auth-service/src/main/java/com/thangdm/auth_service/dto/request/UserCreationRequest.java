package com.thangdm.auth_service.dto.request;

import com.thangdm.auth_service.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationRequest {
    @Size(min = 3, message = "USER_USERNAME_ERR")
    String username;

    @Size(min = 8, message = "USER_PASSWORD_ERR")
    String password;

    String firstName;
    String lastName;

    @DobConstraint(min=18,message = "INVALID_DOB")
    LocalDate dob;
}
