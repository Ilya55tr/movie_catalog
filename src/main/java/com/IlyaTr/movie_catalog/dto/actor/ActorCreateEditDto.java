package com.IlyaTr.movie_catalog.dto.actor;
import com.IlyaTr.movie_catalog.dto.CreateEditObject;
import com.IlyaTr.movie_catalog.validator.annotation.AllFieldsNotEmpty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
public class ActorCreateEditDto extends CreateEditObject {

    private String fullName;

    private LocalDate birthDate;
    @Builder.Default
    private Set<Integer> moviesIds = new HashSet<>();

    private MultipartFile image;
}
