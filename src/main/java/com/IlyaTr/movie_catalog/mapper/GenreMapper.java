package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.entities.Genre;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    @Mapping(target = "image", ignore = true)
    Genre toEntity(GenreCreateEditDto genre);

    GenreReadDto toReadDto(Genre genre);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "image", ignore = true)
    Genre updateEntity(GenreCreateEditDto genreDto, @MappingTarget Genre genre);
}
