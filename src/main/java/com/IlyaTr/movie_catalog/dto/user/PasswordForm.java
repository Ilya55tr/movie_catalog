package com.IlyaTr.movie_catalog.dto.user;

import com.IlyaTr.movie_catalog.validator.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordForm {
    @NotBlank(message = "Старый пароль обязателен")
    private String oldPassword;

    @ValidPassword
    private String newPassword;
    private String confirmPassword;
}
