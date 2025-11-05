package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.repositories.filter.repositories.FilterMovieRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Integer>, FilterMovieRepository,
        QuerydslPredicateExecutor<Movie> {

    Page<Movie> findMoviesByGenreId(Integer genreId, Pageable pageable);
    Set<Movie> findAllByFilter(MovieFilter filter);

    Page<Movie> findMoviesByActors_Id(Integer actorId, Pageable pageable);

}
