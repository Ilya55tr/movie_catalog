package com.IlyaTr.movie_catalog.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class ActorCreateEditDto {
    @NotEmpty
    private String full_name;
    @NotNull
    private LocalDate birth_date;
    @Builder.Default
    private Set<Integer> moviesId = new HashSet<>();
}
