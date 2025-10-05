--liquibase formatted sql

--changeset ilyatr:1-insert-test-genre
INSERT INTO genre (name) VALUES
                             ('Action'),
                             ('Drama'),
                             ('Comedy'),
                             ('Sci-Fi');

--changeset ilyatr:2-insert-test-actor
INSERT INTO actor (full_name, birth_year) VALUES
                                              ('Keanu Reeves', 1964),
                                              ('Leonardo DiCaprio', 1974),
                                              ('Matt Damon', 1970),
                                              ('Emma Stone', 1988),
                                              ('Ryan Gosling', 1980);

--changeset ilyatr:2-insert-test-movie
INSERT INTO movie (title, release_year, rating, description, genre_id) VALUES
                                                                           ('The Matrix', 1999, 8.7, 'A hacker learns the truth about his reality.', 4),
                                                                           ('Inception', 2010, 8.8, 'Dreams within dreams.', 4),
                                                                           ('La La Land', 2016, 8.0, 'Love story between a jazz pianist and an actress.', 3),
                                                                           ('The Martian', 2015, 8.0, 'An astronaut stranded on Mars.', 2),
                                                                           ('John Wick', 2014, 7.4, 'An ex-hitman seeks revenge.', 1);

--changeset ilyatr:3-insert-test-movie_actor
INSERT INTO movie_actor (movie_id, actor_id) VALUES
                                                 (1, 1),
                                                 (2, 2), (2, 3),
                                                 (3, 4), (3, 5),
                                                 (4, 3),
                                                 (5, 1);