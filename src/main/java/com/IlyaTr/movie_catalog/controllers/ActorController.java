package com.IlyaTr.movie_catalog.controllers;


import com.IlyaTr.movie_catalog.dto.PageResponse;
import com.IlyaTr.movie_catalog.dto.actor.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.filter.ActorFilter;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.services.ActorService;
import com.IlyaTr.movie_catalog.services.MovieService;
import com.IlyaTr.movie_catalog.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;
    private final MovieService movieService;
    private final UserService userService;

    @GetMapping("/create")
    public String createActor(Model model){
        model.addAttribute("movies", movieService.findAll());
        if (model.getAttribute("actor") == null){
            model.addAttribute("actor", new ActorCreateEditDto());
        }
        return "actor/create";
    }

    @PostMapping
    public String createActor(@ModelAttribute @Validated ActorCreateEditDto actorDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if (actorDto.getImage().isEmpty()){
            bindingResult.rejectValue("image","imageNotFound", "добавьте изображение");
        }
        if (actorDto.getImage().isEmpty()|| bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("actor", actorDto);
            return "redirect:/actors/create";
        }
        ActorReadDto actor = actorService.createActor(actorDto);
        return "redirect:/actors/"+actor.getId();
    }
    @PostMapping("/{id}/update")
    public String updateActor(@PathVariable Integer id,
                              @ModelAttribute @Validated ActorCreateEditDto actorDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
//            redirectAttributes.addFlashAttribute("actor", actorDto);
            return "redirect:/actors/"+ id;
        }
        ActorReadDto actor = actorService.updateActor(actorDto,id);
        return "redirect:/actors/"+ actor.getId();
    }

    @GetMapping("/{id}")
    public String getActor(Model model,
                           @PathVariable Integer id,
                           @PageableDefault(size = 3) Pageable pageable,
                           Authentication auth,
                           HttpServletRequest request){
        Page<MovieShortDto> movies = movieService.findMoviesByActorId(id, pageable);
        model.addAttribute("movies", PageResponse.of(movies));
        model.addAttribute("all_movies", movieService.findAll());
        model.addAttribute("actor", actorService.findById(id));
        if (auth!=null){
            model.addAttribute("isAuth",
                    auth.isAuthenticated());
            List<Integer> moviesIds =  userService
                    .getFavoritesMovies(auth.getName())
                    .stream().map(MovieShortDto::getId).toList();
            model.addAttribute("favoriteMoviesIds", moviesIds);

            String currentUrl = request.getRequestURI() +
                                (request.getQueryString() != null ? "?" + request.getQueryString() : "");
            model.addAttribute("currentUrl", currentUrl);
        }
        return "actor/actor";
    }

    @GetMapping
    public String getAllActors(Model model, ActorFilter actorFilter, @PageableDefault(size = 3) Pageable pageable){
        model.addAttribute("actors", PageResponse.of(actorService.findAll(actorFilter, pageable)));
        model.addAttribute("actorFilter", actorFilter);
        return "actor/allActors";
    }

    @PostMapping("{id}/update/addMovies")
    public String addMoviesToActor(@PathVariable Integer id,
                                   @ModelAttribute ActorCreateEditDto actorDto){
        ActorReadDto actor = actorService.addMoviesToActor(id, actorDto.getMoviesIds());
        return "redirect:/actors/" + actor.getId();
    }

    @PostMapping("{id}/update/removeMovies")
    public String removeMoviesFromActor(@PathVariable Integer id,
                                   @ModelAttribute  ActorCreateEditDto actorDto){
        ActorReadDto actor = actorService.removeMoviesFromActor(id, actorDto.getMoviesIds());
        return "redirect:/actors/" + actor.getId();
    }

    @PostMapping("{id}/delete")
    public String deleteActor(@PathVariable Integer id){
       if (!actorService.deleteActor(id)){
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
       return "redirect:/actors";
    }

}
