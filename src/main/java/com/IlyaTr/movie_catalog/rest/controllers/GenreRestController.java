package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("api/v1/genre")
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;
    private final ImageService imageService;

    @GetMapping("/{subDir}/{fileName:.+}")
    public Resource getImage(@PathVariable String subDir
            , @PathVariable String fileName){
        Resource image = imageService.load(fileName,subDir);
        return image;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GenreReadDto createGenre(@RequestBody @Validated GenreCreateEditDto genreDto){
        return genreService.createGenre(genreDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreReadDto updateGenre(@RequestBody @Validated GenreCreateEditDto genreDto,
                                    @PathVariable Integer id){
        return genreService.updateGenre(genreDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Integer id){
        if (!genreService.deleteGenre(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreReadDto findGenreById(@RequestParam Integer id){
        return genreService.findById(id);
    }

    @GetMapping
    public Set<GenreReadDto> findAllGenres(){
        return genreService.findAll();
    }

}
