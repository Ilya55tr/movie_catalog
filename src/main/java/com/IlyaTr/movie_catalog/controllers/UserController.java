package com.IlyaTr.movie_catalog.controllers;


import com.IlyaTr.movie_catalog.configuration.KeycloakConfiguration;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.dto.user.PasswordForm;
import com.IlyaTr.movie_catalog.dto.user.UserReadDto;
import com.IlyaTr.movie_catalog.dto.user.UserRegistrationDto;
import com.IlyaTr.movie_catalog.dto.user.UserUpdateDto;
import com.IlyaTr.movie_catalog.entities.Gender;
import com.IlyaTr.movie_catalog.entities.SubscriptionType;
import com.IlyaTr.movie_catalog.entities.User;
import com.IlyaTr.movie_catalog.services.KeycloakService;
import com.IlyaTr.movie_catalog.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.ClientErrorException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final KeycloakService keycloakService;

    @GetMapping("/registration")
    public String getRegisterForm(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        model.getAttribute("exception");
        return "user/registration";
    }


    @PostMapping("/registration")
    public String register(@ModelAttribute("user") @Validated UserRegistrationDto userDto,
                           BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("user", userDto);
            return "user/registration";
        }
        try {
            var user =keycloakService.createUser(userDto);
           log.info("✅ User created successfully");
           userService.findById(user.getId());
           return "redirect:/oauth2/authorization/keycloak";
        }catch (ClientErrorException e){
            if (e.getResponse().getStatus() == 409){
                log.error("Failed to return response", e);
                model.addAttribute("exception",
                        "Пользователь с таким username или email уже существует ");
                model.addAttribute("user", userDto);
                return "user/registration";
            }else{
                log.error("Ошибка при регистрации пользователя", e);
                model.addAttribute("exception",
                        "Неизвестная ошибка при регистрации");
                model.addAttribute("user", userDto);
                return "user/registration";
            }
        }

    }

    @GetMapping("/profile")
    public String getProfile(Authentication auth,
                             Model model,
                             HttpServletRequest request) {


        model.addAttribute("user", userService.findById(auth.getName()));
        model.getAttribute("exception");
        model.addAttribute("subscriptionTypes", SubscriptionType.values());
        model.addAttribute("genders", Gender.values());

        model.addAttribute("favoriteMovies", userService.getFavoritesMovies(auth.getName()));
//        model.getAttribute("errors");
//        model.getAttribute("success");
//        model.getAttribute("sendMessage");
        log.info(auth.getAuthorities().toString());
        return "user/profile";
    }


    @PostMapping("/removeMovie/{movieId}")
    public String removeMoviesFromUser(Authentication auth, @PathVariable Integer movieId){
        userService.removeFavoritesMovies(auth.getName(), movieId);
        return "redirect:/users/profile";
    }


    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute @Valid UserUpdateDto user,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Authentication auth){
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            return "redirect:/users/profile";
        }
        try {
            userService.updateUser(auth.getName(), user);
            redirectAttributes.addFlashAttribute("success", "Изменения внесены успешно ");
            return "redirect:/users/profile";
        }catch (ClientErrorException e){
            if (e.getResponse().getStatus() == 409){
                log.error("Failed to return response", e);
                redirectAttributes.addFlashAttribute("exception",
                        "Пользователь с таким username или email уже существует ");
            }else{
                log.error("Ошибка при регистрации пользователя", e);
                redirectAttributes.addFlashAttribute("exception", "Неизвестная ошибка при регистрации");
            }
            return "redirect:/users/profile";
        }
        }

    @PostMapping("/profile/changePassword")
    public String changePassword(@ModelAttribute @Valid PasswordForm passwordForm,
                                 BindingResult result,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes){
        if (!Objects.equals(passwordForm.getNewPassword(), passwordForm.getConfirmPassword()))
        {
            result.rejectValue("confirmPassword",
                    "nonConfirmPassword",
                    "Пароли не совпадают");
        }
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("password_errors", result.getAllErrors());
            return "redirect:/users/profile";
        }
        try {
            keycloakService.changePassword(passwordForm, auth.getName());
            redirectAttributes.addFlashAttribute("success_password", "Пароль успешно изменен!!!");
            return "redirect:/users/profile";
        }catch (Exception ex){
            redirectAttributes.addFlashAttribute("exception_password",
                    "старый пароль не верен");
            return "redirect:/users/profile";
        }

    }

    @PostMapping("/send-message")
    public String setVerifyMessage(Authentication auth, RedirectAttributes redirectAttributes){
        keycloakService.sendVerifyMessage(auth.getName());
        redirectAttributes.addFlashAttribute("sendMessage", "отправили письмо с подтвержением");
        return "redirect:/users/profile";
    }
}
