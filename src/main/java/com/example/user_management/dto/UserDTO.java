package com.example.user_management.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private String id;
    private String firstname;
    private String lastname;
    private String password;
    private String username;
    private String email;
    private boolean accountLocked;
    private boolean enabled;
    private LocalDateTime createdDate;
    private KYCVerificationDTO kyc; // Include KYC DTO
}

