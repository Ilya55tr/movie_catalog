--liquibase formatted sql

--changeset ilyatr:4-add-image-to-actor-and-movie
ALTER TABLE actor
    ADD COLUMN image VARCHAR(128);

ALTER TABLE movie
    ADD COLUMN image VARCHAR(128);

--changeset ilyatr:5-add-image-to-genre
ALTER TABLE genre
    ADD COLUMN image VARCHAR(128);
