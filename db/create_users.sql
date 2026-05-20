

-- Create users TABLE
-- auto-generated definition
create table users
(
    id        integer not null
        constraint users_pk
            primary key,
    username  TEXT    not null,
    firstname TEXT    not null,
    lastname  TEXT    not null,
    email     TEXT    not null,
    password  TEXT    not null
);

