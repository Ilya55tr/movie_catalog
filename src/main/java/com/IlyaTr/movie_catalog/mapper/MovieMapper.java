package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.entities.Movie;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, ActorMapper.class})
public interface MovieMapper {

    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "image", ignore = true)
    Movie toEntity(MovieCreateEditDto movieDto);

    MovieReadDto toReadDto(Movie movie);

    MovieShortDto toShortDto(Movie movie);

    @Mapping(target = "genre", ignore = true)
    @Mapping( target = "actors", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "image", ignore = true)
    Movie updateEntity(MovieCreateEditDto movieDto, @MappingTarget Movie movie);

}
