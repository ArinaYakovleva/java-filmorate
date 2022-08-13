create table if not exists age_restrictions (
    restriction_id int generated by default as identity primary key,
    restriction_name varchar
);

create table if not exists films (
    film_id        int generated by default as identity primary key,
    name           varchar,
    description    varchar(200),
    release_date   date,
    duration       int,
    restriction_id int references age_restrictions(restriction_id)
);

create table if not exists users (
    user_id  int generated by default as identity primary key,
    email    varchar,
    login    varchar,
    name     varchar,
    birthday date
);

create table if not exists likes (
    film_id int references films (film_id),
    user_id int references users (user_id),
    primary key (film_id, user_id)
);

create table if not exists friends (
    user_id   int references users (user_id),
    friend_id int references users (user_id),
    primary key (user_id, friend_id)
);
create table if not exists genres (
    genre_id int generated by default as identity primary key,
    genre_name varchar
);

create table if not exists genres_of_film (
    film_id int references films (film_id),
    genre_id int references genres (genre_id),
    primary key (film_id, genre_id)
);