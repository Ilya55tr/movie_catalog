package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.dto.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("api/v1/movie")
@RequiredArgsConstructor
public class MovieRestController {

    private final MovieService movieService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieReadDto createMovie(@RequestBody @Validated MovieCreateEditDto movieDto){
        return movieService.createMovie(movieDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieReadDto updateMovie(@RequestBody @Validated MovieCreateEditDto movieDto,
                                    @PathVariable Integer id){
        return movieService.updateMovie(movieDto, id);
    }

    @PatchMapping("/{id}/addActors")
    @ResponseStatus(HttpStatus.OK)
    public MovieReadDto addActorsToMovie(@PathVariable Integer id, @RequestBody Set<Integer> newActorsIds){
        return movieService.addActorsToMovie(id,newActorsIds);
    }

    @PatchMapping("/{id}/removeActors")
    @ResponseStatus(HttpStatus.OK)
    public MovieReadDto removeActorsFromMovie(@PathVariable Integer id,@RequestBody Set<Integer> removeActorsIds){
        return movieService.removeActorsFromMovie(id, removeActorsIds);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Integer id){
        if (!movieService.deleteMovie(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieReadDto findById(@PathVariable Integer id){
        return movieService.findMovieById(id);
    }

    @GetMapping
    public Set<MovieReadDto> findAll(){
        return movieService.findAll();
    }

}
