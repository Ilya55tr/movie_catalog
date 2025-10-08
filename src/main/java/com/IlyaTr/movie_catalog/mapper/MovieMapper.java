package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.MovieShortDto;
import com.IlyaTr.movie_catalog.entities.Movie;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, ActorMapper.class})
public interface MovieMapper {

    @Mapping(target = "genre", ignore = true)
    @Mapping(target = "actors", ignore = true)
    Movie toEntity(MovieCreateEditDto movieDto);

    MovieReadDto toReadDto(Movie movie);

    MovieShortDto toShortDto(Movie movie);

    @Mapping(target = "genre", ignore = true)
    @Mapping( target = "actors", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Movie updateEntity(MovieCreateEditDto movieDto, @MappingTarget Movie movie);

}
