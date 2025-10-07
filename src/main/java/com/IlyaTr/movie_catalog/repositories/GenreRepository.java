package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
