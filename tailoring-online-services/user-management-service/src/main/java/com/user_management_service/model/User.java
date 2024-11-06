package com.user_management_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user_management_service.enums.*;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain alphanumeric characters and underscores")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[!@#$%^&*].*", message = "Password must contain at least one special character")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain letters")
    private String lastName;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{1,4}\\)?[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,9}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String profilePicture;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @DateOfBirthValidation(message = "User must be at least 18 years old")
    private Date dateOfBirth;

    private Date lastLogin;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Language preference cannot be null")
    private LanguagePreference languagePreference;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AgeValidator.class)
    public @interface DateOfBirthValidation {
        String message() default "User must be at least 18 years old";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class AgeValidator implements ConstraintValidator<DateOfBirthValidation, Date> {

        private String message;

        @Override
        public void initialize(DateOfBirthValidation constraintAnnotation) {
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Date dateOfBirth, ConstraintValidatorContext context) {
            if (dateOfBirth == null) {
                return true;
            }
            LocalDate dob = dateOfBirth.toLocalDate();
            LocalDate now = LocalDate.now();
            int age = Period.between(dob, now).getYears();

            if (age < 18) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.message).addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
