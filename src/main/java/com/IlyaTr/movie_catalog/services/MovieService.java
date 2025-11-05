package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.QPredicates;
import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.mapper.MappingHelperImpl;
import com.IlyaTr.movie_catalog.mapper.MovieMapper;
import com.IlyaTr.movie_catalog.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.IlyaTr.movie_catalog.entities.QMovie.movie;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MappingHelperImpl mappingHelperImpl;

    @Transactional
    public MovieReadDto createMovie(MovieCreateEditDto movieDto){
        return Optional.of(movieDto)
                .map(movieMapper::toEntity)
                .map(movie -> {
                    Set<Actor> actorSet = mappingHelperImpl.actorsIdsToActors(movieDto.getActorsIds());
                    for (Actor actor: actorSet){
                        movie.addActor(actor);
                    }
                    movie.setGenre(mappingHelperImpl.genreIdToGenre(movieDto.getGenreId()));
                    return movie;
                })
                .map(movie -> mappingHelperImpl.setImage(movie, movieDto, "movies"))
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
        Set<Actor> actors = mappingHelperImpl.actorsIdsToActors(actorIds);
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

        Set<Actor> actorsToRemove = mappingHelperImpl.actorsIdsToActors(actorIds);
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

                    movie.setGenre(mappingHelperImpl.genreIdToGenre(movieDto.getGenreId()));
                    movieMapper.updateEntity(movieDto,movie);
                    return movie;
                })
                .map(movie -> mappingHelperImpl.setImage(movie, movieDto, "movies"))
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

    public Set<MovieReadDto> findAll(MovieFilter filter){
        return movieRepository
                .findAllByFilter(filter)
                .stream()
                .map(movieMapper::toReadDto)
                .collect(Collectors.toSet());
    }

    public Page<MovieReadDto> findAll(MovieFilter filter, Pageable pageable){
        var predicates = QPredicates.builder()
                .add(filter.title(), movie.title::containsIgnoreCase)
                .add(filter.releaseYear(), movie.releaseYear::eq)
                .add(filter.rating(), movie.rating::goe)
                .add(filter.genre(), movie.genre.id::eq).build();

        return movieRepository
                .findAll(predicates, pageable)
                .map(movieMapper::toReadDto);
    }

    public Page<MovieShortDto> findMoviesByGenreId(Integer genreId, Pageable pageable){
        return movieRepository
                .findMoviesByGenreId(genreId, pageable)
                .map(movieMapper::toShortDto);
    }

    public Page<MovieShortDto> findMoviesByActorId(Integer actorId, Pageable pageable){
        return movieRepository
                .findMoviesByActors_Id(actorId, pageable)
                .map(movieMapper::toShortDto);
    }



}
