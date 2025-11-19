package com.IlyaTr.movie_catalog.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       http
               .authorizeHttpRequests(auth->

                auth.requestMatchers("/movies/**","/genres/**", "/actors/**",
                                "/users/registration","api/v1/image/**")
                        .permitAll()
                        .requestMatchers("users/profile/**")
                        .hasAnyAuthority("movies_catalog.user", "movies_catalog.admin" )
                        .anyRequest().authenticated())
               .oauth2Login(oauth2 -> {
                   oauth2.userInfoEndpoint(userInfo-> userInfo.oidcUserService(oAuth2UserService()));
                   oauth2.defaultSuccessUrl("/movies",true);
                   oauth2.loginPage("/oauth2/authorization/keycloak");
               })
               .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler())
                       .invalidateHttpSession(true)
                       .clearAuthentication(true)
                       .deleteCookies("JSESSIONID"));
       return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(){
        return (request, response, authentication) -> {


            if (authentication != null && authentication.getPrincipal() instanceof OidcUser oidcUser) {
                String idToken = oidcUser.getIdToken().getTokenValue();
                String logoutUrl = "http://localhost:8090/realms/movies_catalog/protocol/openid-connect/logout"
                                   + "?id_token_hint=" + URLEncoder.encode(idToken, StandardCharsets.UTF_8)
                                   + "&post_logout_redirect_uri=" + URLEncoder.encode("http://localhost:8091/movies", StandardCharsets.UTF_8);

                response.sendRedirect(logoutUrl);
            }else {

                response.sendRedirect("http://localhost:8091/movies");
            }
        };
    }


    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
        return userRequest -> {
            OidcUser oidcUser = new OidcUserService().loadUser(userRequest);
            List<String> roles = oidcUser.getClaimAsStringList("roles");
            log.warn("User roles: {}", roles );
            if (roles == null) {
                roles = Collections.emptyList();
            }
            var mappedAuthorities =  roles.stream()
                    .filter(role -> role.startsWith("movies_catalog."))
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .collect(Collectors.toList());
            log.warn("mappedAuthorities: {}", mappedAuthorities );

            return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
        };
    }


//    private JwtAuthenticationConverter jwtAuthenticationConverter() {
//        var converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthenticationConverter());
//        converter.setPrincipalClaimName("preferred_username");
//        return converter;
//    }
}
