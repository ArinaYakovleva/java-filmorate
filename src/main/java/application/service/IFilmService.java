package application.service;

import application.model.AgeRestriction;
import application.model.Film;
import application.model.Genre;

import java.util.Collection;

public interface IFilmService extends IService<Film> {
    void likeFilm(int filmId, int userId);

    void dislikeFilm(int filmId, int userId);

    Collection<Film> getMostPopularFilms(Integer count);

    Collection<AgeRestriction> getAgeRestrictions();

    AgeRestriction getAgeRestrictionById(int id);

    Collection<Genre> getGenres();

    Genre getGenreById(int genreId);
}
