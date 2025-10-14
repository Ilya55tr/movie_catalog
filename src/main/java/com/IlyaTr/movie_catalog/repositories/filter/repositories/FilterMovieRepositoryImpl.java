package com.IlyaTr.movie_catalog.repositories.filter.repositories;
import com.IlyaTr.movie_catalog.dto.QPredicates;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.entities.QMovie;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.IlyaTr.movie_catalog.entities.QMovie.*;


@RequiredArgsConstructor
public class FilterMovieRepositoryImpl implements FilterMovieRepository{

    private final EntityManager entityManager;

    @Override
    public Set<Movie> findAllByFilter(MovieFilter filter) {
        var predicates = QPredicates.builder()
                .add(filter.title(), movie.title::containsIgnoreCase)
                .add(filter.releaseYear(), movie.releaseYear::eq)
                .add(filter.rating(), movie.rating::goe)
                .add(filter.genre(), movie.genre.id::eq).build();

        return new HashSet<>(new JPAQuery<>(entityManager)
                .select(movie)
                .from(movie)
                .where(predicates)
                .fetch());
    }
}
