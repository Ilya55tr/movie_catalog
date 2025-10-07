package com.IlyaTr.movie_catalog.dto;

import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Genre;

import lombok.*;

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
    private String title;
    private Integer releaseYear;
    private BigDecimal rating;
    private String description;

    private Integer genreId;
    @Builder.Default
    private Set<Integer> actorsIds = new HashSet<>();

}
