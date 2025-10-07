package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
