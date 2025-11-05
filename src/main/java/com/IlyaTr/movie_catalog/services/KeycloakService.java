package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.configuration.KeycloakConfiguration;
import com.IlyaTr.movie_catalog.dto.user.PasswordForm;
import com.IlyaTr.movie_catalog.dto.user.UserRegistrationDto;
import com.IlyaTr.movie_catalog.dto.user.UserUpdateDto;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakConfiguration keycloakConfiguration;

    public String getAdminToken(){
        return keycloak.tokenManager().getAccessTokenString();
    }

    public UserRepresentation createUser(UserRegistrationDto userDto){
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDto.getUsername());
        user.setEmailVerified(false);
        user.setEmail(userDto.getEmail());
        user.setEnabled(true);
        user.setClientRoles(Map.of("movies_catalog", List.of("testapp.user")));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        if (userDto.getBirthDate()!=null){
            user.singleAttribute("birthDate", userDto.getBirthDate().toString());
        }
        if (userDto.getGender() != null) {
            user.singleAttribute("gender", userDto.getGender().toString());
        }
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        user.setCredentials((List.of(credential)));

        Response response = keycloak
                .realm("movies_catalog")
                .users()
                .create(user);

        int status = response.getStatus();
        if (status == 201) {
            log.info("✅ User created successfully");
            response.close();
            return user;
        } else if (status == 409) {
            log.warn("⚠️ User already exists in Keycloak (status 409)");
            throw new ClientErrorException("User already exists", 409);
        } else {
            String body = response.readEntity(String.class);
            log.error("❌ Failed to create user in Keycloak. Status: {}, Body: {}", status, body);
            throw new ClientErrorException("Keycloak error: " + body, status);
        }

    }

    public UserRepresentation findUserById(String sub){
        return Optional.of(keycloak.realm("movies_catalog")
                .users()
                .get(sub)
                .toRepresentation()).orElseThrow(()-> new NotFoundException("User not found"));
    }

    public void updateUser(String sub, UserUpdateDto updatedUser){
        var user = findUserById(sub);

        if (!Objects.equals(updatedUser.getEmail(), user.getEmail())){
            user.setEmail(updatedUser.getEmail());
            user.setEmailVerified(false);
        }
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        if (updatedUser.getGender() != null) {
            user.singleAttribute("gender", updatedUser.getGender().toString());
        }
        user.setUsername(updatedUser.getUsername());

        if (updatedUser.getBirthDate() != null) {
            user.singleAttribute("birthDate", updatedUser.getBirthDate().toString());
        }

        keycloak.realm("movies_catalog")
                .users()
                .get(sub)
                .update(user);
    }

    public void sendVerifyMessage(String sub){
        keycloak.realm("movies_catalog")
                .users()
                .get(sub)
                .sendVerifyEmail();
    }
    public void changePassword(PasswordForm passwordForm, String id){

        var user = findUserById(id);
        attemptPasswordVerification(user.getUsername(), passwordForm.getOldPassword());
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(passwordForm.getNewPassword());
        cred.setTemporary(false);
        keycloak.realm("movies_catalog").users().get(user.getId()).resetPassword(cred);
    }

    private void attemptPasswordVerification(String username, String oldPassword){
       try{
           Keycloak tempKeycloak = KeycloakBuilder.builder()
                   .serverUrl(keycloakConfiguration.keycloakUrl)
                   .realm(keycloakConfiguration.realm)
                   .clientId(keycloakConfiguration.clientId)
                   .clientSecret(keycloakConfiguration.clientSecret)
                   .username(username)
                   .password(oldPassword)
                   .build();
           tempKeycloak.tokenManager().getAccessToken();
           tempKeycloak.close();
       }catch (Exception e){

           throw new RuntimeException("Current password is incorrect");
       }

    }

}
