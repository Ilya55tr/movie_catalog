package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;

    @GetMapping("/create")
    public String createMovie(Model model){
        if (model.getAttribute("movie")==null){
            model.addAttribute("movie", new MovieCreateEditDto());
        }
        model.addAttribute("genres",genreService.findAll());
        model.addAttribute("actors", actorService.findAll());
        return "movie/create";
    }

    @PostMapping
    public String createMovie(@ModelAttribute @Validated MovieCreateEditDto movieDto, Model model, RedirectAttributes redirectAttributes){
        if (!movieDto.getImage().isEmpty()){
            MovieReadDto movie = movieService.createMovie(movieDto);
            return "redirect:/movies/"+ movie.getId();
        }
        redirectAttributes.addFlashAttribute("movie", movieDto);
        return "redirect:/movies/create";
    }

    @PostMapping("/{id}/update")
    public String updateMovie(@ModelAttribute @Validated MovieCreateEditDto movieDto,
                              @PathVariable Integer id){
        MovieReadDto movie = movieService.updateMovie(movieDto, id);
        return "redirect:/movies/"+ movie.getId();
    }

    @PostMapping("/{id}/update/addActors")
    public String addActorsToMovie(@ModelAttribute MovieCreateEditDto movieDto,
                                   @PathVariable Integer id){
        MovieReadDto movie = movieService.addActorsToMovie(id, movieDto.getActorsIds());
        return "redirect:/movies/"+ + movie.getId();
    }

    @PostMapping("/{id}/update/removeActors")
    public String removeActorsFromMovie(@ModelAttribute MovieCreateEditDto movieDto,
                                        @PathVariable Integer id){
        MovieReadDto movie =movieService.removeActorsFromMovie(id, movieDto.getActorsIds());
        return "redirect:/movies/"+ movie.getId();
    }

    @GetMapping
    public String getAll(Model model, MovieFilter filter){
        model.addAttribute("movies",movieService.findAll());
//        model.addAttribute("movies",movieService.findAll(filter));
        return "movie/allMovies";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable Integer id){
        model.addAttribute("movie", movieService.findMovieById(id));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("actors", actorService.findAll());
        return "movie/movie";
    }

    @PostMapping("{id}/delete")
    public String deleteMovie(@PathVariable Integer id){
        if (!movieService.deleteMovie(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/movies";
    }




}
