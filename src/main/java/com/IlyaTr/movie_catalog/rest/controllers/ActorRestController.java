package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.dto.actor.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("api/v1/actor")
@RequiredArgsConstructor
public class ActorRestController {
    private final ActorService actorService;
    private final ImageService imageService;

    @GetMapping("/{subDir}/{fileName}")
    public Resource getImage(@PathVariable String subDir, @PathVariable String fileName){
        Resource image = imageService.load(fileName, subDir);
        return image;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActorReadDto createActor(@RequestBody ActorCreateEditDto actorDto){
        return actorService.createActor(actorDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActorReadDto updateActor(@PathVariable Integer id, @RequestBody ActorCreateEditDto actorDto){
        return actorService.updateActor(actorDto, id);
    }

    @PatchMapping("/{id}/addMovies")
    @ResponseStatus(HttpStatus.OK)
    public ActorReadDto addMoviesToActor(@PathVariable Integer id, @RequestBody Set<Integer> moviesIds){
        return actorService.addMoviesToActor(id, moviesIds);
    }

    @PatchMapping("/{id}/removeMovies")
    @ResponseStatus(HttpStatus.OK)
    public ActorReadDto removeMoviesFromActor(@PathVariable Integer id, @RequestBody Set<Integer> moviesIds){
        return actorService.removeMoviesFromActor(id, moviesIds);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(@PathVariable Integer id){
        if (!actorService.deleteActor(id)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ActorReadDto findById(@PathVariable Integer id){
        return actorService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Set<ActorReadDto> findAll(){
        return actorService.findAll();
    }

}
