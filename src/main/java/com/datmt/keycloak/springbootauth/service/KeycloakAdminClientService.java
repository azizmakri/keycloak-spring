package com.datmt.keycloak.springbootauth.service;

import com.datmt.keycloak.springbootauth.config.KeycloakProvider;
import com.datmt.keycloak.springbootauth.http.requests.CreateUserRequest;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class KeycloakAdminClientService {
    @Value("${keycloak.realm}")
    public String realm;

    private final KeycloakProvider kcProvider;


    public KeycloakAdminClientService(KeycloakProvider keycloakProvider) {
        this.kcProvider = keycloakProvider;
    }

    public Response createKeycloakUser(CreateUserRequest user) {
        UsersResource usersResource = kcProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstname());
        kcUser.setLastName(user.getLastname());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(true);



        Response response = usersResource.create(kcUser);
        //String userId = extractCreatedUserId(response);
/*
        // Set client roles for the user
        ClientResource clientResource = kcProvider.getInstance().realm(realm).clients().getByClientId(clientId);
        RoleRepresentation roleRepresentation = clientResource.roles().get(roleName).toRepresentation();
        List<RoleRepresentation> roles = new ArrayList<>();
        roles.add(roleRepresentation);
        clientResource.users().get(userId).roles().clientLevel(clientId).add(roles);*/

        if (response.getStatus() == 201) {
            //If you want to save the user to your other database, do it here, for example:
//            User localUser = new User();
//            localUser.setFirstName(kcUser.getFirstName());
//            localUser.setLastName(kcUser.getLastName());
//            localUser.setEmail(user.getEmail());
//            localUser.setCreatedDate(Timestamp.from(Instant.now()));
//            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
//            usersResource.get(userId).sendVerifyEmail();
//            userRepository.save(localUser);
        }

        return response;

    }
    /*
    private String extractCreatedUserId(Response response) {
        String location = response.getHeaderString("Location");
        String[] segments = location.split("/");
        return segments[segments.length - 1];
    }*/

    private static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }


}
