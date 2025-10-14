package com.IlyaTr.movie_catalog.dto.movie;

import com.IlyaTr.movie_catalog.dto.CreateEditObject;
import com.IlyaTr.movie_catalog.validator.annotation.AllFieldsNotEmpty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@AllFieldsNotEmpty
public class MovieCreateEditDto extends CreateEditObject {

    private String title;
    private Integer releaseYear;
    @Positive
    @Max(10)
    private BigDecimal rating;
    private String description;
    private Integer genreId;
    @Builder.Default
    private Set<Integer> actorsIds = new HashSet<>();
    private MultipartFile image;
}
