package application.storage.dao;

import application.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface IGenreStorage {
    Collection<Genre> getGenres();
    Optional<Genre> getGenreById(int genreId);
}
