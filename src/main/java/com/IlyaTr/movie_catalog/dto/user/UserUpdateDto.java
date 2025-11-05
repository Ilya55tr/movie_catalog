package com.IlyaTr.movie_catalog.dto.user;


import com.IlyaTr.movie_catalog.entities.Gender;
import com.IlyaTr.movie_catalog.entities.SubscriptionType;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserUpdateDto {
    @NotBlank(message = "username обязателен")
    private String username;

    private String firstName;

    private String lastName;

    private Gender gender;
    @Email(message = "Введите корректный email")
    @NotBlank(message = "Email обязателен")
    private String email;


    private LocalDate birthDate;

    @NotNull(message = "Тип подписки обязателен")
    private SubscriptionType subscriptionType;
    private MultipartFile image;
    private boolean verifyEmail;
}
