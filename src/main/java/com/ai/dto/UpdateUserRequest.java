package com.ai.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String password;
}
