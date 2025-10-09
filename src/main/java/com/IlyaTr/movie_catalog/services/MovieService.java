package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.mapper.MappingHelper;
import com.IlyaTr.movie_catalog.mapper.MovieMapper;
import com.IlyaTr.movie_catalog.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MappingHelper mappingHelper;

    @Transactional
    public MovieReadDto createMovie(MovieCreateEditDto movieDto){
        return Optional.of(movieDto)
                .map(movieMapper::toEntity)
                .map(movie -> {
                    Set<Actor> actorSet = mappingHelper.actorsIdsToActors(movieDto.getActorsIds());
                    for (Actor actor: actorSet){
                        movie.addActor(actor);
                    }
                    movie.setGenre(mappingHelper.genreIdToGenre(movieDto.getGenreId()));
                    return movie;
                })
                .map(movieRepository::save)
                .map(movieMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public boolean deleteMovie(Integer id){
        return movieRepository.findById(id)
                .map(movie -> {
                    movieRepository.delete(movie);
                    movieRepository.flush();
                    return true;
                }).orElse(false);
    }

    @Transactional
    public MovieReadDto addActorsToMovie(Integer movieId, Set<Integer> actorIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow();
        Set<Actor> actors = mappingHelper.actorsIdsToActors(actorIds);
        for (Actor actor : actors) {
            movie.addActor(actor);
        }

        return Optional.of(movieRepository.save(movie))
                .map(movieMapper::toReadDto)
                .orElseThrow();
    }
    @Transactional
    public MovieReadDto removeActorsFromMovie(Integer movieId, Set<Integer> actorIds) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));

        Set<Actor> actorsToRemove = mappingHelper.actorsIdsToActors(actorIds);
        for (Actor actor : actorsToRemove) {
            movie.removeActor(actor);
        }
        return Optional.of(movieRepository.save(movie))
                .map(movieMapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public MovieReadDto updateMovie(MovieCreateEditDto movieDto, Integer id){
        return movieRepository.findById(id)
                .map(movie -> {

                    movie.setGenre(mappingHelper.genreIdToGenre(movieDto.getGenreId()));
                    movieMapper.updateEntity(movieDto,movie);
                    return movie;
                })
                .map(movieRepository::save)
                .map(movieMapper::toReadDto)
                .orElseThrow();
    }

    public MovieReadDto findMovieById(Integer id){
        return movieRepository.findById(id)
                .map(movieMapper::toReadDto)
                .orElseThrow();
    }

    public Set<MovieReadDto> findAll(){
        return movieRepository
                .findAll()
                .stream()
                .map(movieMapper::toReadDto)
                .collect(Collectors.toSet());
    }


}
