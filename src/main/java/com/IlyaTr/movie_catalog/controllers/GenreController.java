package com.IlyaTr.movie_catalog.controllers;

import com.IlyaTr.movie_catalog.dto.PageResponse;
import com.IlyaTr.movie_catalog.dto.genre.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.genre.GenreReadDto;
import com.IlyaTr.movie_catalog.services.GenreService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

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
    public String getGenre(@PathVariable Integer id, Model model) {
        model.addAttribute("genre", genreService.findById(id));
//        model.addAttribute("imageName", genreService.findById(id).getImage());
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