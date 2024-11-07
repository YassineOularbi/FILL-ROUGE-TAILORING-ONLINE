package com.user_management_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user_management_service.enums.*;
import com.user_management_service.validation.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;
import java.lang.annotation.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @NotBlank(message = "Username cannot be empty", groups = {CreateGroup.class})
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters", groups = {CreateGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain alphanumeric characters and underscores", groups = {CreateGroup.class})
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @PasswordValidation(message = "Password cannot contain username or email", groups = {CreateGroup.class, UpdateGroup.class} )
    @NotBlank(message = "Password cannot be empty", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(min = 8, message = "Password must be at least 8 characters long", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = ".*[!@#$%^&*].*", message = "Password must contain at least one special character", groups = {CreateGroup.class, UpdateGroup.class})
    private String password;

    @NotBlank(message = "Email cannot be empty", groups = {CreateGroup.class})
    @Email(message = "Invalid email format", groups = {CreateGroup.class})
    private String email;

    @NotNull(message = "Role cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    private Role role;

    @NotBlank(message = "First name cannot be empty", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 50, message = "First name cannot exceed 50 characters", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters", groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;

    @NotBlank(message = "Last name cannot be empty", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 50, message = "Last name cannot exceed 50 characters", groups = {CreateGroup.class, UpdateGroup.class})
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain letters", groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{1,4}\\)?[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,9}$", message = "Invalid phone number format", groups = {CreateGroup.class, UpdateGroup.class})
    private String phoneNumber;

    @URL(message = "Profile picture must be a valid URL", groups = {CreateGroup.class, UpdateGroup.class})
    private String profilePicture = "https://res.cloudinary.com/dqegda2km/image/upload/v1730966537/sxdg6zledc1fjvwxs61q.webp";

    @NotNull(message = "Date of birth cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    @Past(message = "Date of birth must be in the past", groups = {CreateGroup.class, UpdateGroup.class})
    @DateOfBirthValidation(message = "User must be at least 18 years old")
    private LocalDate dateOfBirth;

    @NotNull(message = "Last login cannot be null", groups = {UpdateGroup.class})
    private LocalDateTime lastLogin;

    @NotNull(message = "Status cannot be null", groups = {UpdateGroup.class})
    private Status status = Status.INACTIVE;

    @NotNull(message = "Language preference cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    private LanguagePreference languagePreference;

    @NotNull(message = "Gender cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    private Gender gender;

    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AgeValidator.class)
    public @interface DateOfBirthValidation {
        String message() default "User must be at least 18 years old";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class AgeValidator implements ConstraintValidator<DateOfBirthValidation, LocalDate> {

        private String message;

        @Override
        public void initialize(DateOfBirthValidation constraintAnnotation) {
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
            if (dateOfBirth == null) {
                return true;
            }

            int age = Period.between(dateOfBirth, LocalDate.now()).getYears();

            if (age < 18) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(this.message).addConstraintViolation();
                return false;
            }

            return true;
        }
    }

    @Target({ ElementType.FIELD, ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PasswordValidator.class)
    public @interface PasswordValidation {
        String message() default "Password cannot contain username or email";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            if (password == null) {
                return true;
            }
            Tailor tailor = (Tailor) context.unwrap(Tailor.class);
            String username = tailor.getUsername();
            String email = tailor.getEmail();
            return !password.contains(username) && (email == null || !password.contains(email));
        }
    }
}
