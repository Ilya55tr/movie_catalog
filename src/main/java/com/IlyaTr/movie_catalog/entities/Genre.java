package com.IlyaTr.movie_catalog.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre")
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();

    private String image;



}
