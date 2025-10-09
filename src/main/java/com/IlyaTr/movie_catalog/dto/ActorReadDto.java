package com.IlyaTr.movie_catalog.dto;

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

}
