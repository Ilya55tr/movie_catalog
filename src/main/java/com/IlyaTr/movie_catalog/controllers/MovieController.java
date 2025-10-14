package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.PageResponse;
import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.MovieService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

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
    public String createMovie(@ModelAttribute @Validated MovieCreateEditDto movieDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes){
        if (movieDto.getImage().isEmpty()){
            bindingResult.rejectValue("image","imageNotFound", "добавьте изображение");
        }
        if (movieDto.getImage().isEmpty()||bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("movie", movieDto);
            return "redirect:/movies/create";
        }
        MovieReadDto movie = movieService.createMovie(movieDto);
        return "redirect:/movies/"+ movie.getId();
    }

    @PostMapping("/{id}/update")
    public String updateMovie(@ModelAttribute @Validated MovieCreateEditDto movieDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @PathVariable Integer id){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/movies/"+ id;
        }
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
    public String getAll(Model model, MovieFilter filter, @PageableDefault(size = 3) Pageable pageable){
        Page<MovieReadDto> page =movieService.findAll(filter, pageable);
        model.addAttribute("movies", PageResponse.of(page));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("filter", filter);
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
