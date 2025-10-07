package com.IlyaTr.movie_catalog.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GenreReadDto {
    Integer id;
    String name;
}
