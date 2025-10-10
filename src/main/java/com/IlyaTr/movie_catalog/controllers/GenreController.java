package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/create")
    public String createGenre(Model model){
        model.addAttribute("genre", new GenreCreateEditDto());
        return "genre/create";
    }

    @PostMapping
    public String createGenre(@ModelAttribute GenreCreateEditDto genreDto, RedirectAttributes redirectAttributes){
       GenreReadDto genreReadDto=genreService.createGenre(genreDto);
//        if (true){
//            redirectAttributes.addFlashAttribute("genre", genreDto);
//            return "redirect:/genres/create";
//        }
        return "redirect:/genres/"+ genreReadDto.getId();
    }

    @GetMapping("/{id}")
    public String getGenre(@PathVariable Integer id, Model model){
        model.addAttribute("genre", genreService.findById(id));
        return "genre/genre";
    }

    @GetMapping
    public String getAllGenres(Model model){
        model.addAttribute("genres", genreService.findAll());
        return "genre/allGenres";
    }

    @PostMapping("/{id}/update")
    public String updateGenre(@PathVariable Integer id, @ModelAttribute GenreCreateEditDto genreDto){
        genreService.updateGenre(genreDto, id);
        return "redirect:/genres/"+id;
    }

    @PostMapping("/{id}/delete")
    public String deleteGenre(@PathVariable Integer id){
        if (!genreService.deleteGenre(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/genres";
    }
}