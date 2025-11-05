package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.PageResponse;
import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieCreateEditDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieReadDto;
import com.IlyaTr.movie_catalog.dto.filter.MovieFilter;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.entities.Actor;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.MovieService;
import com.IlyaTr.movie_catalog.services.UserService;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;
    private final UserService userService;


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
    public String getAll(Authentication auth,
                         Model model,
                         MovieFilter filter,
                         @PageableDefault(size = 3) Pageable pageable,
                         HttpServletRequest request){
        Page<MovieReadDto> page =movieService.findAll(filter, pageable);
        model.addAttribute("movies", PageResponse.of(page));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("filter", filter);
        if (auth!=null){
            model.addAttribute("isAuth", auth.isAuthenticated());
            List<Integer> moviesIds =  userService
                    .getFavoritesMovies(auth.getName())
                    .stream().map(MovieShortDto::getId).toList();
            model.addAttribute("favoriteMoviesIds", moviesIds);

            String currentUrl = request.getRequestURI() +
                                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            model.addAttribute("currentUrl", currentUrl);
            log.warn("currentUrl = {}", currentUrl);
            log.warn("isAuth = {}", auth.isAuthenticated());
        }

        return "movie/allMovies";
    }

    @PostMapping("/addMovie/{movieId}")
    public String addMoviesToUser(Authentication auth, @PathVariable Integer movieId,
                                  @RequestParam(required = false) String redirectUrl){
        userService.addFavoritesMovies(auth.getName(), movieId);
        log.warn("RedirectUrl = {}",redirectUrl);
        return "redirect:"+(redirectUrl != null ? redirectUrl : "/movies");
    }

    @PostMapping("/removeMovie/{movieId}")
    public String removeMoviesFromUser(Authentication auth, @PathVariable Integer movieId,
                                       @RequestParam(required = false) String redirectUrl){
        userService.removeFavoritesMovies(auth.getName(), movieId);
        return "redirect:"+ (redirectUrl != null ? redirectUrl : "/movies");
    }

    @GetMapping("/{id}")
    public String getById(Model model,
                          @PathVariable Integer id,
                          @PageableDefault(size = 3) Pageable pageable){
        model.addAttribute("movie", movieService.findMovieById(id));
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("all_actors", actorService.findAll());
        Page<ActorReadDto> page = actorService.getActorsByMovieId(id, pageable);
        model.addAttribute("actors", PageResponse.of(page));

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
