package com.IlyaTr.movie_catalog.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "movies")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(length = 150, nullable = false, unique = true, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "actors")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
