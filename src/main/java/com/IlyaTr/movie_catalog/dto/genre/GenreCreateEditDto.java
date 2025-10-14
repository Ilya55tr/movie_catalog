package com.IlyaTr.movie_catalog.dto.genre;


import com.IlyaTr.movie_catalog.dto.CreateEditObject;
import com.IlyaTr.movie_catalog.validator.annotation.AllFieldsNotEmpty;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AllFieldsNotEmpty
public class GenreCreateEditDto extends CreateEditObject {
    private String name;
    private MultipartFile image;
}
