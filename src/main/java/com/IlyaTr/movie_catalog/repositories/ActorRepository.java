package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer>{
}
