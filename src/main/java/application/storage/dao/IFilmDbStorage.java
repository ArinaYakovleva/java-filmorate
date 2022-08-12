package application.storage.dao;

import application.model.AgeRestriction;
import application.model.Film;
import application.model.Genre;
import application.storage.Storage;

import java.util.Collection;
import java.util.Optional;

public interface IFilmDbStorage extends Storage<Film> {
    void likeFilm(int filmId, int userId);
    void dislikeFilm(int filmId, int userId);
    Collection<Film> getMostPopularFilms (Integer count);
    Collection<Genre> getGenres();
    Optional<Genre> getGenreById(int genreId);
    Collection<AgeRestriction> getAgeRestrictions();
    Optional<AgeRestriction> getAgeRestrictionById(int id);

}
