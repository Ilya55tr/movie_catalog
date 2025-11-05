package com.IlyaTr.movie_catalog.repositories;


import com.IlyaTr.movie_catalog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

}
