package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

    private final ImageService imageService;

    @GetMapping("/{subDir}/{fileName:.+}")
    @ResponseStatus(HttpStatus.OK)
    public Resource getImage(@PathVariable String subDir,
                             @PathVariable String fileName){
        Resource image = imageService.load(fileName, subDir);
        return image;
    }
}
