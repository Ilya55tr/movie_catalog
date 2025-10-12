package com.IlyaTr.movie_catalog.controllers;


import com.IlyaTr.movie_catalog.dto.ActorCreateEditDto;
import com.IlyaTr.movie_catalog.dto.ActorReadDto;
import com.IlyaTr.movie_catalog.services.ActorService;
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
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;
    private final MovieService movieService;

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
                              RedirectAttributes redirectAttributes){
        if (!actorDto.getImage().isEmpty()){
            ActorReadDto actor = actorService.createActor(actorDto);
            return "redirect:/actors/"+actor.getId();
        }
        redirectAttributes.addFlashAttribute("actor", actorDto);
        return "redirect:/actors/create";
    }
    @PostMapping("/{id}/update")
    public String updateActor(@ModelAttribute @Validated ActorCreateEditDto actorDto, @PathVariable Integer id){
        ActorReadDto actor = actorService.updateActor(actorDto,id);
        return "redirect:/actors/"+ actor.getId();
    }

    @GetMapping("/{id}")
    public String getActor(Model model, @PathVariable Integer id){
        model.addAttribute("actor", actorService.findById(id));
        model.addAttribute("movies", movieService.findAll());
        return "actor/actor";
    }

    @GetMapping
    public String getAllActors(Model model){
        model.addAttribute("actors", actorService.findAll());
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
