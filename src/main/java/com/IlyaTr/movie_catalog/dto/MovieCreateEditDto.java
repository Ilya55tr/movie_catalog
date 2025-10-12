package com.IlyaTr.movie_catalog.dto;

import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Genre;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieCreateEditDto {
    @NotNull
    private String title;
    @NotNull
    private Integer releaseYear;
    @Positive
    @Max(10)
    private BigDecimal rating;
    private String description;
    @NotNull
    private Integer genreId;
    @Builder.Default
    private Set<Integer> actorsIds = new HashSet<>();
    private MultipartFile image;
}
