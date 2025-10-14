package com.IlyaTr.movie_catalog.repositories;

import com.IlyaTr.movie_catalog.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ActorRepository extends JpaRepository<Actor, Integer>,
        QuerydslPredicateExecutor<Actor> {
}
