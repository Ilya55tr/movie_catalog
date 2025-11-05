package com.IlyaTr.movie_catalog.dto.user;

import com.IlyaTr.movie_catalog.entities.Gender;
import com.IlyaTr.movie_catalog.validator.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank(message = "username обязателен")
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    @Email(message = "Введите корректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @ValidPassword
    @NotBlank(message = "Пароль обязателен")
    private String password;
    private LocalDate birthDate;
}
