package application.service;

import application.model.Film;

import java.util.Collection;

public interface FilmService extends Service<Film> {
    void likeFilm(int filmId, int userId);

    void dislikeFilm(int filmId, int userId);

    Collection<Film> getMostPopularFilms(Integer count);
}
