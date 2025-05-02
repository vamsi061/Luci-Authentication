package com.ai.model;

import jakarta.persistence.*;
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

    @Column(nullable=false, name="full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    // Extra profile fields
    private LocalDate dob;
    private String phone;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;
}
