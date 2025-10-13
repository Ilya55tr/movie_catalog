package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
//    Set<Movie> findAllByFilter(MovieFilter filter);
}
