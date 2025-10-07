package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.entities.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    Genre toEntity(GenreCreateEditDto genre);

    GenreReadDto toReadDto(Genre genre);

    Genre updateEntity(GenreCreateEditDto genreDto, @MappingTarget Genre genre);
}
