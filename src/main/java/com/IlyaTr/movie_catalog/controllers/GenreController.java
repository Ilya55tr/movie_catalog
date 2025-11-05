package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.PageResponse;
import com.IlyaTr.movie_catalog.dto.genre.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.genre.GenreReadDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.services.GenreService;
import com.IlyaTr.movie_catalog.services.MovieService;
import com.IlyaTr.movie_catalog.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.List;

@Controller
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;
    private final MovieService movieService;
    private final UserService userService;

    @GetMapping("/create")
    public String createGenre(Model model) {
        if ( model.getAttribute("genre")==null){
            model.addAttribute("genre", new GenreCreateEditDto());
        }

        return "genre/create";
    }

    @PostMapping
    public String createGenre(@ModelAttribute @Validated GenreCreateEditDto genreDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (genreDto.getImage().isEmpty()){
            bindingResult.rejectValue("image","imageNotFound", "добавьте изображение");
        }
        if (genreDto.getImage().isEmpty()||bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("genre", genreDto);
            return "redirect:/genres/create";
        }

        GenreReadDto genre = genreService.createGenre(genreDto);
        return "redirect:/genres/" + genre.getId();


    }

    @GetMapping("/{id}")
    public String getGenre(@PathVariable Integer id,
                           Model model,
                           @PageableDefault(size = 3) Pageable pageable,
                           Authentication auth,
                           HttpServletRequest request) {
        model.addAttribute("genre", genreService.findById(id));
        Page<MovieShortDto> page = movieService.findMoviesByGenreId(id, pageable);
        model.addAttribute("movies", PageResponse.of(page));
//        model.addAttribute("imageName", genreService.findById(id).getImage());

        if (auth!=null){
            model.addAttribute("isAuth", auth.isAuthenticated());
            List<Integer> moviesIds =  userService
                    .getFavoritesMovies(auth.getName())
                    .stream().map(MovieShortDto::getId).toList();
            model.addAttribute("favoriteMoviesIds", moviesIds);

            String currentUrl = request.getRequestURI() +
                                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            model.addAttribute("currentUrl", currentUrl);
        }
        return "genre/genre";
    }

    @GetMapping
    public String getAllGenres(Model model, @PageableDefault(size = 3) Pageable pageable) {
        model.addAttribute("genres", PageResponse.of(genreService.findAll(pageable)));
        return "genre/allGenres";
    }

    @PostMapping("/{id}/update")
    public String updateGenre(@PathVariable Integer id,
                              @ModelAttribute @Validated GenreCreateEditDto genreDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/genres/" + id;
        }
        genreService.updateGenre(genreDto, id);
        return "redirect:/genres/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteGenre(@PathVariable Integer id) {
        if (!genreService.deleteGenre(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/genres";
    }
}