package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.entities.Genre;
import com.IlyaTr.movie_catalog.entities.Movie;

import java.util.Set;

public interface MappingHelper {


    Movie setImage(Movie movie, MovieCreateEditDto movieDto, String subDir);

    Genre setImage(Genre genre, GenreCreateEditDto genreDto, String subDir);

    Actor setImage(Actor actor, ActorCreateEditDto actorDto, String subDir);
}
