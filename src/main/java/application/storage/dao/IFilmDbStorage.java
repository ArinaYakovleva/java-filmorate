package application.storage.dao;

import application.model.Film;
import application.storage.Storage;

import java.util.Collection;

public interface IFilmDbStorage extends Storage<Film> {
    void likeFilm(int filmId, int userId);

    void dislikeFilm(int filmId, int userId);

    Collection<Film> getMostPopularFilms(Integer count);
}
