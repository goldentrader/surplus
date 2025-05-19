package com.example.user_management.dto;

import com.example.user_management.enums.Role;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private String id;  // lowercase
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private LocalDate dateOfBirth;
    private boolean accountLocked;
    private Role role;
    private boolean enabled;
    private LocalDateTime createdDate;
    private KYCVerificationDTO kyc;
}
