package com.IlyaTr.movie_catalog.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MovieShortDto {
    private Integer id;
    private String title;
    private Integer releaseYear;
    private String image;
}
