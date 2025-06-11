package com.example.user_management.service;

import com.example.user_management.dto.UserCreateDTO;
import com.example.user_management.entity.User;
import com.example.user_management.enums.Role;
import com.example.user_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUserToMongo(UserCreateDTO dto, String keycloakUserId) {
        dto.setId(keycloakUserId);  // <-- assign Keycloak user ID here

        User user = User.builder()
                .id(dto.getId())  // now the Mongo _id will be Keycloak userId
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .enabled(dto.isEnabled())
                .accountLocked(dto.isAccountLocked())
                .createdDate(dto.getCreatedDate() != null ? dto.getCreatedDate() : LocalDateTime.now())
                .role(dto.getRole() != null ? dto.getRole() : Role.USER)
                .build();

        userRepository.save(user);
    }


}
