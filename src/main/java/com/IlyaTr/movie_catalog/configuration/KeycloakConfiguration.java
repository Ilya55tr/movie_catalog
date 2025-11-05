package com.IlyaTr.movie_catalog.configuration;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

    @Value("${keycloak.admin.url}")
    public String keycloakUrl;
    @Value("${keycloak.admin.realm}")
    public  String realm;
    @Value("${keycloak.admin.client-id}")
    public String clientAdminId;
    @Value("${keycloak.admin.client-secret}")
    public  String clientAdminSecret;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    public  String clientSecret;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    public  String clientId;

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .clientId(clientAdminId)
                .clientSecret(clientAdminSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

}
