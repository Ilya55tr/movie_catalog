package com.IlyaTr.movie_catalog.services;

import com.IlyaTr.movie_catalog.dto.GenreCreateEditDto;
import com.IlyaTr.movie_catalog.dto.GenreReadDto;
import com.IlyaTr.movie_catalog.entities.Genre;
import com.IlyaTr.movie_catalog.mapper.GenreMapper;
import com.IlyaTr.movie_catalog.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreReadDto createGenre(GenreCreateEditDto genreDto){
        return Optional.of(genreDto)
                .map(genreMapper::toEntity)
                .map(genreRepository::save)
                .map(genreMapper::toReadDto)
                .orElseThrow(() -> new RuntimeException("Failed to create Genre" + genreDto.getName()));
    }

    @Transactional
    public GenreReadDto updateGenre(GenreCreateEditDto genreDto, Integer id){
        return  genreRepository.findById(id)
                .map(genre -> genreMapper.updateEntity(genreDto,genre))
                .map(genreRepository::save)
                .map(genreMapper::toReadDto).orElseThrow(() -> new RuntimeException("Failed to update Genre id:" + id));
    }

    @Transactional
    public boolean deleteGenre(Integer id){
        return genreRepository.findById(id)
                .map(genre -> {
                    genreRepository.delete(genre);
                    genreRepository.flush();
                    return true;
                }).orElse(false);
    }

     public GenreReadDto findById(Integer id){
        return genreRepository.findById(id)
                .map(genreMapper::toReadDto)
                .orElseThrow(() -> new RuntimeException("Failed to find Genre id:" + id));
    }

    public Set<GenreReadDto> findAll(){
        return genreRepository.findAll()
                .stream().map(genreMapper::toReadDto).collect(Collectors.toSet());
    }

}
