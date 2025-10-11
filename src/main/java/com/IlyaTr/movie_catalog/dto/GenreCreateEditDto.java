package com.IlyaTr.movie_catalog.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GenreCreateEditDto{
    @NotEmpty
    private String name;
    private MultipartFile image;
}
