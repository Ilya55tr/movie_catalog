package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.actor.ActorReadDto;
import com.IlyaTr.movie_catalog.dto.movie.MovieShortDto;
import com.IlyaTr.movie_catalog.dto.user.UserReadDto;
import com.IlyaTr.movie_catalog.dto.user.UserUpdateDto;
import com.IlyaTr.movie_catalog.entities.Movie;
import com.IlyaTr.movie_catalog.entities.User;
import com.IlyaTr.movie_catalog.mapper.MovieMapper;
import com.IlyaTr.movie_catalog.mapper.UserMapper;
import com.IlyaTr.movie_catalog.repositories.ActorRepository;
import com.IlyaTr.movie_catalog.repositories.MovieRepository;
import com.IlyaTr.movie_catalog.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final  KeycloakService keycloakService;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final ActorRepository actorRepository;

    @Transactional
    public UserReadDto findById(String id){
        return userRepository.findById(id)
                .map(user -> userMapper.toReadDto(user, id))
                .orElseGet(()->userMapper.createToReadDto(id));
    }

    @Transactional
    public UserReadDto updateUser(String id, UserUpdateDto userDto){
        keycloakService.updateUser(id, userDto);
        User user = userRepository.save(userMapper.updateUser(userDto, id));
        return userMapper.toReadDto(user, id);
    }

    @Transactional
    public void addFavoritesMovies(String userId, Integer movieId){
        User user =userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        user.addMovie(movie);
    }

    @Transactional
    public void removeFavoritesMovies(String userId, Integer movieId){
        User user =userRepository.findById(userId).orElseThrow();
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        user.removeMovie(movie);
    }

    public List<MovieShortDto> getFavoritesMovies(String userId){
        return userRepository
                .findById(userId).orElseThrow()
                .getMovies().stream()
                .map(movieMapper::toShortDto)
                .toList();
    }
}
