package com.ai.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UserProfileDto {
    private String phoneNumber;
    private LocalDate dob;
    private MultipartFile profilePic; // optional custom pic

    // getters and setters
}
