package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.repositories.filter.repositories.FilterMovieRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Integer>, FilterMovieRepository,
        QuerydslPredicateExecutor<Movie> {

    Set<Movie> findAllByFilter(MovieFilter filter);
}
