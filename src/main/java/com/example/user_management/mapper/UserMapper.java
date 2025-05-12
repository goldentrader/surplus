package com.example.user_management.mapper;

import com.example.user_management.dto.UserDTO;
import com.example.user_management.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final KYCVerificationMapper kycVerificationMapper;

    public UserMapper(KYCVerificationMapper kycVerificationMapper) {
        this.kycVerificationMapper = kycVerificationMapper;
    }

    // Convert User entity to UserDTO
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setAccountLocked(user.isAccountLocked());
        userDTO.setEnabled(user.isEnabled());
        userDTO.setCreatedDate(user.getCreatedDate());

        // Map KYCVerification to KYCVerificationDTO
        if (user.getKyc() != null) {
            userDTO.setKyc(kycVerificationMapper.toKYCVerificationDTO(user.getKyc()));
        }

        // Assuming that password is stored separately and not part of this mapping
        // If the password should be mapped, add userDTO.setPassword(user.getPassword()) here

        return userDTO;
    }

    // Convert UserDTO to User entity (if needed for saving back to DB)
    public User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setAccountLocked(userDTO.isAccountLocked());
        user.setEnabled(userDTO.isEnabled());
        user.setCreatedDate(userDTO.getCreatedDate());

        // Map KYCVerificationDTO to KYCVerification
        if (userDTO.getKyc() != null) {
            user.setKyc(kycVerificationMapper.toKYCVerification(userDTO.getKyc()));
        }

        // Handle password mapping here if it's part of the entity
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            // Assuming you want to store password in the entity (or do something with it)
            // Here you can set the password into the user entity
            // user.setPassword(userDTO.getPassword());
        }

        return user;
    }
}
