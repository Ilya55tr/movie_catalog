package com.IlyaTr.movie_catalog.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GenreReadDto {
    private Integer id;
    private String name;
    private String image;

    @Builder.Default
    private Set<MovieShortDto> movies = new HashSet<>();
}
