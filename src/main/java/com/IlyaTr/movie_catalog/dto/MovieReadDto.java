package com.IlyaTr.movie_catalog.dto;

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
public class MovieReadDto {
    private Integer id;
    private String title;
    private Integer releaseYear;
    private BigDecimal rating;
    private String description;
    private GenreReadDto genre;
    private String image;
    @Builder.Default
    private Set<ActorReadDto> actors = new HashSet<>();
}
