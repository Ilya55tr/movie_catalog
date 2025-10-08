package com.IlyaTr.movie_catalog.rest.controllers;

import com.IlyaTr.movie_catalog.services.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/actors")
@RequiredArgsConstructor
public class ActorRestController {
    private final ActorService actorService;

}
