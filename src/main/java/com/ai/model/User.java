package com.ai.model;

import com.ai.util.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    // Extra profile fields
    private LocalDate dob;
    private String phone;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;
}
