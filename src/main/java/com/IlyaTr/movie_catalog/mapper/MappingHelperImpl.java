package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Genre;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.repositories.ActorRepository;
import com.IlyaTr.movie_catalog.repositories.GenreRepository;
import com.IlyaTr.movie_catalog.repositories.MovieRepository;
import com.IlyaTr.movie_catalog.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MappingHelperImpl implements MappingHelper {
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final ImageService imageService;


    public Genre genreIdToGenre(Integer id){
        if (id == null) {
            return null;
        }
        return genreRepository.findById(id)
                .orElseThrow(() ->new RuntimeException("Genre not found with id:"+ id));
    }


    public Set<Actor> actorsIdsToActors(Set<Integer> ids){
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream().map(id -> actorRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("Actor not found with id:" +id)))
                .collect(Collectors.toSet());
    }


    public Set<Movie> moviesIdsToMovies(Set<Integer> ids){
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        return ids.stream().map(id -> movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found with id:" +id)))
                .collect(Collectors.toSet());
    }

    @Override
    public Genre setImage(Genre genre, GenreCreateEditDto genreDto, String subDir ){
        if (!genreDto.getImage().isEmpty()){
            genre.setImage(imageService.upload(genreDto.getImage(), subDir));
        }
        return genre;
    }


    @Override
    public Movie setImage(Movie movie, MovieCreateEditDto movieDto, String subDir){
        if (!movieDto.getImage().isEmpty()){
            movie.setImage(imageService.upload(movieDto.getImage(), subDir));
        }
        return movie;
    }
    @Override
    public Actor setImage(Actor actor, ActorCreateEditDto actorDto, String subDir){
        if (!actorDto.getImage().isEmpty()){
            actor.setImage(imageService.upload(actorDto.getImage(), subDir));
        }
        return actor;
    }
}
