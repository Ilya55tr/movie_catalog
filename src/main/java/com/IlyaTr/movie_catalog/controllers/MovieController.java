package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.mapper.MappingHelper;
import com.IlyaTr.movie_catalog.mapper.MovieMapper;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;

    @GetMapping("/create")
    public String createMovie(Model model){

        model.addAttribute(genreService.findAll());
        model.addAttribute(actorService.findAll());
        return "movie/create";
    }

    @PostMapping
    public String createMovie(@ModelAttribute MovieCreateEditDto movieDto, Model model){
        MovieReadDto movie = movieService.createMovie(movieDto);
        model.addAttribute(genreService.findAll());
        model.addAttribute(actorService.findAll());
        return "redirect:/movies/"+ movie.getId();
    }

    @PostMapping("/{id}/update")
    public String updateMovie(@ModelAttribute MovieCreateEditDto movieDto){

    }

    @GetMapping
    public String getAll(){

    }

    @GetMapping("{id}")
    public String getById(){

    }

    @PostMapping("{id}/delete")
    public String deleteMovie(){

    }


}
