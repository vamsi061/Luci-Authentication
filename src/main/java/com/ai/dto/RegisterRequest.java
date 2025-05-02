package com.ai.dto;

import com.ai.util.ValidPassword;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @Column(nullable = false, name = "full_name")
    @NotBlank(message = "Full name must not be empty")
    @Pattern(regexp = "^(?!\\s*$)[A-Za-z\\s'-]+$", message = "Full name must not contain numbers or special characters")
    private String fullName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @ValidPassword
    private String password;
}
