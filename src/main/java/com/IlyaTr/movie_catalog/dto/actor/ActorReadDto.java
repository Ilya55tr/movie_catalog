package com.IlyaTr.movie_catalog.dto.actor;

import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ActorReadDto {
    private Integer id;
    private String fullName;
    private LocalDate birthDate;
    @Builder.Default
    private Set<MovieShortDto> movies = new HashSet<>();
    private String image;
}
