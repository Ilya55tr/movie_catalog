--liquibase formatted sql

--changeset ilyatr:1-create-genre-table
CREATE TABLE genre (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL
);
--changeset ilyatr:2-create-actor-table
CREATE TABLE actor (
                       id SERIAL PRIMARY KEY,
                       full_name VARCHAR(150) NOT NULL,
                       birth_year INT
);

--changeset ilyatr:3-create-movie-table
CREATE TABLE movie (
                       id SERIAL PRIMARY KEY,
                       title VARCHAR(200) NOT NULL,
                       release_year INT NOT NULL,
                       rating DECIMAL(2,1),
                       description TEXT,
                       genre_id INT REFERENCES genre(id) ON DELETE SET NULL
);

--changeset ilyatr:4-create-movie_actor-table
CREATE TABLE movie_actor (
                             movie_id INT REFERENCES movie(id) ON DELETE CASCADE,
                             actor_id INT REFERENCES actor(id) ON DELETE CASCADE,
                             PRIMARY KEY (movie_id, actor_id)
);

