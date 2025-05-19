package com.example.user_management.controller;

import com.example.user_management.dto.UserCreateDTO;
import com.example.user_management.service.KeycloakService;
import com.example.user_management.service.UserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final KeycloakService keycloakService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserCreateDTO userCreateDTO) {
        // Step 1: Register in Keycloak
        Response kcResponse = keycloakService.registerUser(userCreateDTO);

        if (kcResponse.getStatus() != 201) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Keycloak registration failed: " + kcResponse.getStatusInfo());
        }

        // Step 2: Register in MongoDB
        userService.saveUserToMongo(userCreateDTO);

        return ResponseEntity.ok("User registered successfully");
    }
}
