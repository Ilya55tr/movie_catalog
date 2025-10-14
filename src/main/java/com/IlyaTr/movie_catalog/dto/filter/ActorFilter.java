package com.IlyaTr.movie_catalog.dto.filter;

import com.IlyaTr.movie_catalog.entities.Movie;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ActorFilter {

    String fullName;
    LocalDate birthdate;
    String movieTitle;
    boolean similarSearch;
}
