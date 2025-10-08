package com.IlyaTr.movie_catalog.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GenreCreateEditDto{
    @NotEmpty
    private String name;
}
