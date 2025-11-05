--liquibase formatted sql

--changeset ilyatr:1-create-users-table
CREATE TABLE users (
                       id VARCHAR(255) PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       subscriptionType VARCHAR(100),
                       image VARCHAR(256),
                       email VARCHAR(256)
);