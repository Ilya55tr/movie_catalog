package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.ActorReadDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.mapper.ActorMapper;
import com.IlyaTr.movie_catalog.mapper.MappingHelperImpl;
import com.IlyaTr.movie_catalog.repositories.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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


}
