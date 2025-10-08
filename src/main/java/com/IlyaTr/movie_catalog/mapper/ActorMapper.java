package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.ActorReadDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    ActorReadDto toReadDto(Actor actor);

    @Mapping(target = "movies", ignore = true)
    Actor toEntity(ActorCreateEditDto actorDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "movies", ignore = true)
    Actor updateEntity(ActorCreateEditDto actorDto, @MappingTarget Actor actor);

}
