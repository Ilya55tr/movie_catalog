package com.IlyaTr.movie_catalog.dto.user;


import com.IlyaTr.movie_catalog.entities.Gender;
import com.IlyaTr.movie_catalog.entities.SubscriptionType;
import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadDto {
    private String username;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String email;
    private LocalDate birthDate;
    private SubscriptionType subscriptionType;
    private String image;
    private boolean verifyEmail;
}
