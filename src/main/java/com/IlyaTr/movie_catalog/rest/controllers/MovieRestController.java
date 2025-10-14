package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import com.IlyaTr.movie_catalog.services.ImageService;
import com.IlyaTr.movie_catalog.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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
    private final ImageService imageService;

    @GetMapping("/{subDir}/{fileName:.+}")
    @ResponseStatus(HttpStatus.OK)
    public Resource getImage(@PathVariable String subDir,
                             @PathVariable String fileName){
        Resource image = imageService.load(fileName, subDir);
        return image;
    }
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
