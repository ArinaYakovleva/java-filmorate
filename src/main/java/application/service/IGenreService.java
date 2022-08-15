package application.service;

import application.model.Genre;

import java.util.Collection;

public interface IGenreService {
    Collection<Genre> getGenres();
    Genre getGenreById(int genreId);
}
