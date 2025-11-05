package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.user.UserReadDto;
import com.IlyaTr.movie_catalog.dto.user.UserUpdateDto;
import com.IlyaTr.movie_catalog.entities.Gender;
import com.IlyaTr.movie_catalog.entities.User;
import com.IlyaTr.movie_catalog.repositories.UserRepository;
import com.IlyaTr.movie_catalog.services.ImageService;
import com.IlyaTr.movie_catalog.services.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import static com.IlyaTr.movie_catalog.entities.SubscriptionType.FREE;


@Component
@RequiredArgsConstructor
public class UserMapper {
    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserReadDto toReadDto(User user, String id){
        var newUser = keycloakService.findUserById(id);
        String birthDateStr = newUser.firstAttribute("birthDate");
        return new UserReadDto(newUser.getUsername(),
                newUser.getFirstName(),
                newUser.getLastName(),
                Optional.ofNullable(newUser.firstAttribute("gender"))
                        .filter(s -> !s.isBlank() && !"null".equalsIgnoreCase(s))
                        .map(Gender::valueOf)
                        .orElse(null),
                newUser.getEmail(),
                birthDateStr != null && !birthDateStr.isBlank() && !birthDateStr.equals("null")
                        ? LocalDate.parse(birthDateStr)
                        : null,
                user.getSubscriptionType(),
                user.getImage(),
                newUser.isEmailVerified());
    }

    public UserReadDto createToReadDto(String id){
        var newUser = keycloakService.findUserById(id);
        User user = new User(newUser.getId(),
                newUser.getUsername(),
                newUser.getEmail(),
                FREE,
                "default.jpg",
                new ArrayList<>());
        userRepository.save(user);
        String birthDateStr = newUser.firstAttribute("birthDate");
        return new UserReadDto(newUser.getUsername(), newUser.getFirstName(),
                newUser.getLastName(),
                Optional.ofNullable(newUser.firstAttribute("gender"))
                        .filter(s -> !s.isBlank() && !"null".equalsIgnoreCase(s))
                        .map(Gender::valueOf)
                        .orElse(null),
                newUser.getEmail(),
                birthDateStr != null && !birthDateStr.isBlank() && !birthDateStr.equals("null")
                        ? LocalDate.parse(birthDateStr)
                        : null,
                user.getSubscriptionType(),
                user.getImage(),
                newUser.isEmailVerified());
    }

    public User updateUser(UserUpdateDto userDto, String id){
        return userRepository.findById(id).map(updatedUser -> {
            updatedUser.setEmail(userDto.getEmail());
            updatedUser.setUsername(userDto.getUsername());
            updatedUser.setSubscriptionType(userDto.getSubscriptionType());
            if (!userDto.getImage().isEmpty()){
                updatedUser.setImage(imageService.upload(userDto.getImage(),"users"));
            }
            return updatedUser;
        }).orElseThrow();

    }
}
