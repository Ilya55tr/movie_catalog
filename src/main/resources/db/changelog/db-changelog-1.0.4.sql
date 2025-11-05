--liquibase formatted sql

--changeset ilyatr:1-create-user_favorites-table
CREATE TABLE user_favorites (
                                user_id VARCHAR(256) REFERENCES users(id) ON DELETE CASCADE,
                                movie_id INT REFERENCES movie(id) ON DELETE CASCADE,
                                PRIMARY KEY(user_id, movie_id)
);