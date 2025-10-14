package com.IlyaTr.movie_catalog.mapper;

import com.IlyaTr.movie_catalog.dto.actor.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    ActorReadDto toReadDto(Actor actor);


    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "image", ignore = true)
    Actor toEntity(ActorCreateEditDto actorDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "movies", ignore = true)
    @Mapping(target = "image", ignore = true)
    Actor updateEntity(ActorCreateEditDto actorDto, @MappingTarget Actor actor);

}
