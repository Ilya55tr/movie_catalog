package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.QPredicates;
import com.IlyaTr.movie_catalog.dto.actor.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.filter.ActorFilter;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.entities.QActor;
import com.IlyaTr.movie_catalog.mapper.ActorMapper;
import com.IlyaTr.movie_catalog.mapper.MappingHelperImpl;
import com.IlyaTr.movie_catalog.repositories.ActorRepository;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.IlyaTr.movie_catalog.entities.QActor.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActorService {
    private final ActorRepository actorRepository;
    private final ActorMapper actorMapper;
    private final MappingHelperImpl mappingHelperImpl;

    @Transactional
    public ActorReadDto createActor(ActorCreateEditDto actorDto) {
        return Optional.of(actorDto)
                .map(actorMapper::toEntity)
                .map(actor -> {
                    Set<Movie> movies = mappingHelperImpl.moviesIdsToMovies(actorDto.getMoviesIds());
                    for (Movie movie : movies) {
                        movie.addActor(actor);
                    }
                    return actor;
                })
                .map(actor -> mappingHelperImpl.setImage(actor, actorDto, "actors"))
                .map(actorRepository::save)
                .map(actorMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public ActorReadDto updateActor(ActorCreateEditDto actorDto, Integer id) {
        return actorRepository.findById(id)
                .map(actor -> {
                    mappingHelperImpl.setImage(actor, actorDto, "actors");
                    actorMapper.updateEntity(actorDto, actor);
                    return actor;
                })
                .map(actorMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public ActorReadDto removeMoviesFromActor(Integer actorId, Set<Integer> moviesIds) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + actorId));

        Set<Movie> moviesToRemove = mappingHelperImpl.moviesIdsToMovies(moviesIds);
        for (Movie movie : moviesToRemove) {
            movie.removeActor(actor);
        }
        return Optional.of(actorRepository.save(actor))
                .map(actorMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public ActorReadDto addMoviesToActor(Integer actorId, Set<Integer> moviesIds) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new RuntimeException("Actor not found with id: " + actorId));

        Set<Movie> moviesToAdd = mappingHelperImpl.moviesIdsToMovies(moviesIds);
        for (Movie movie : moviesToAdd) {
            movie.addActor(actor);
        }
        return Optional.of(actorRepository.save(actor))
                .map(actorMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public boolean deleteActor(Integer id) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actorRepository.delete(actor);
                    actorRepository.flush();
                    return true;
                }).orElse(false);
    }

    public ActorReadDto findById(Integer id) {
        return actorRepository
                .findById(id)
                .map(actorMapper::toReadDto)
                .orElseThrow();
    }

    public Set<ActorReadDto> findAll() {
        return actorRepository
                .findAll().stream()
                .map(actorMapper::toReadDto)
                .collect(Collectors.toSet());
    }

    public Page<ActorReadDto> findAll(ActorFilter actorFilter, Pageable pageable) {
        Predicate basePredicate = QPredicates.builder()
                .add(actorFilter.getFullName(), actor.fullName::containsIgnoreCase)
                .add(actorFilter.getBirthdate(), actor.birthDate::eq).build();

        if (actorFilter.getMovieTitle()!= null && !actorFilter.getMovieTitle().isEmpty()){
            Predicate similarMovie = actor.movies.any().title.containsIgnoreCase(actorFilter.getMovieTitle());
            Predicate exactMovie = actor.movies.any().title.equalsIgnoreCase(actorFilter.getMovieTitle());
            Page<Actor> exactResult = actorRepository.findAll(ExpressionUtils.allOf(basePredicate, exactMovie), pageable);

            if (!exactResult.isEmpty()){
                actorFilter.setSimilarSearch(false);
                return exactResult.map(actorMapper::toReadDto);
            }else{
                actorFilter.setSimilarSearch(true);
                return actorRepository.findAll(ExpressionUtils.allOf(basePredicate, similarMovie), pageable)
                        .map(actorMapper::toReadDto);
            }
        }
        return actorRepository.findAll(basePredicate, pageable)
                .map(actorMapper::toReadDto);
    }

}
