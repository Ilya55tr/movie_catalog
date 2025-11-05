package com.IlyaTr.movie_catalog.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")

public class User {

    @Id
    String id;

    String username;

    String email;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "subscriptiontype")
    SubscriptionType subscriptionType;

    String image;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    List<Movie> movies = new ArrayList<>();

    public void addMovie(Movie movie){
        movies.add(movie);
        movie.getUsers().add(this);
    }

    public void removeMovie(Movie movie){
        movies.remove(movie);
        movie.getUsers().remove(this);
    }



}
