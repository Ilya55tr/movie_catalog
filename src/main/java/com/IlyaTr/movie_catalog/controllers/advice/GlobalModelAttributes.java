package com.IlyaTr.movie_catalog.controllers.advice;

import com.IlyaTr.movie_catalog.dto.user.UserReadDto;
import com.IlyaTr.movie_catalog.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalModelAttributes {

    private final UserService userService;

    @ModelAttribute
    public void addUserToFragment(Model model, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            String keycloakId = oauth2User.getAttribute("sub");
            if (keycloakId != null) {
                UserReadDto user = userService.findById(keycloakId);
                model.addAttribute("currentUser", user);
                model.addAttribute("isAuthenticated", true);
//                model.addAttribute("role", authentication.getAuthorities().);
                return;
            }
        }
        model.addAttribute("currentUser", null);
        model.addAttribute("isAuthenticated", false);
    }
}
