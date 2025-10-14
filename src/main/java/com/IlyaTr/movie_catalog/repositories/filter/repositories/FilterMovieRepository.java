package com.IlyaTr.movie_catalog.repositories.filter.repositories;

import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.entities.Movie;

import java.util.Set;

public interface FilterMovieRepository {
    Set<Movie> findAllByFilter(MovieFilter filter);
}
