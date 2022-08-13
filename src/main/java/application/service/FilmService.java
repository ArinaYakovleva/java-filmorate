package application.service;

import application.model.AgeRestriction;
import application.model.Film;
import application.model.Genre;
import application.storage.dao.IFilmDbStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;

@Service
@Slf4j
public class FilmService extends CommonService<Film, IFilmDbStorage> implements IFilmService {
    @Autowired
    public FilmService(IFilmDbStorage storage) {
        super(storage);
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        log.debug(String.format("Лайк фильма с ID %d у пользователя %d", filmId, userId));
        storage.likeFilm(filmId, userId);
    }

    @Override
    public void dislikeFilm(int filmId, int userId) {
        log.debug(String.format("Удаления лайка фильма с ID %d у пользователя %d", filmId, userId));
        storage.dislikeFilm(filmId, userId);
    }

    @Override
    public Collection<Film> getMostPopularFilms(Integer count) {
        return storage.getMostPopularFilms(count);
    }

    @Override
    public Collection<AgeRestriction> getAgeRestrictions() {
        return storage.getAgeRestrictions();
    }

    @Override
    public AgeRestriction getAgeRestrictionById(int id) {
        return storage.getAgeRestrictionById(id)
                .orElseThrow(() -> new NotFoundException(String.format("MPA c ID %d не найден", id)));
    }

    @Override
    public Collection<Genre> getGenres() {
        return storage.getGenres();
    }

    @Override
    public Genre getGenreById(int genreId) {
        return storage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с ID %d не найден", genreId)));
    }
}
