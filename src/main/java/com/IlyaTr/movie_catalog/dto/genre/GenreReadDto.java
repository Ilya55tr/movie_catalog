package com.IlyaTr.movie_catalog.dto.genre;

import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
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
