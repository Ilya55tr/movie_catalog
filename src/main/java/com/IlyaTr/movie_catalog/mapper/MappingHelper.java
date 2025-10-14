package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.actor.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.genre.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Genre;
import com.IlyaTr.movie_catalog.entities.Movie;

public interface MappingHelper {


    Movie setImage(Movie movie, MovieCreateEditDto movieDto, String subDir);

    Genre setImage(Genre genre, GenreCreateEditDto genreDto, String subDir);

    Actor setImage(Actor actor, ActorCreateEditDto actorDto, String subDir);
}
