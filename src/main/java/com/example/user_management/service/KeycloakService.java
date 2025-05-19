package com.example.user_management.service;

import com.example.user_management.dto.UserCreateDTO;
import com.example.user_management.enums.Role;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public Response registerUser(UserCreateDTO userDTO) {
        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastname());
        user.setCredentials(Collections.singletonList(credentials));
        user.setEnabled(true);

        // Create the user
        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(user);

        if (response.getStatus() == 201 && userDTO.getRole() != null) {
            String userId = extractUserIdFromLocationHeader(response);
            assignRealmRole(userId, userDTO.getRole());
        }

        return response;
    }

    private void assignRealmRole(String userId, Role roleName) {
        RealmResource realmResource = keycloak.realm(realm);

        // Check if role exists
        RoleRepresentation role;
        try {
            role = realmResource.roles().get(String.valueOf(roleName)).toRepresentation();
        } catch (Exception e) {
            throw new RuntimeException("Role '" + roleName + "' does not exist in Keycloak.");
        }

        UserResource userResource = realmResource.users().get(userId);
        userResource.roles().realmLevel().add(Collections.singletonList(role));
    }

    private String extractUserIdFromLocationHeader(Response response) {
        String location = response.getHeaderString("Location");
        if (location == null) {
            throw new RuntimeException("Missing 'Location' header from Keycloak response.");
        }
        return location.replaceAll(".*/([^/]+)$", "$1");
    }
}
