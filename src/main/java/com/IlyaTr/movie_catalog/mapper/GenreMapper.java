package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.entities.Genre;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toEntity(GenreCreateEditDto genre);

    GenreReadDto toReadDto(Genre genre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Genre updateEntity(GenreCreateEditDto genreDto, @MappingTarget Genre genre);
}
