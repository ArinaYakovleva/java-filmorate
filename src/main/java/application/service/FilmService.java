package application.service;

import application.model.Film;
import application.model.User;
import application.storage.dao.IFilmDbStorage;
import application.storage.dao.IUserDbStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class FilmService extends CommonService<Film, IFilmDbStorage> implements IFilmService {
    private final IUserDbStorage userDbStorage;
    @Autowired
    public FilmService(IFilmDbStorage storage, IUserDbStorage userDbStorage) {
        super(storage);
        this.userDbStorage = userDbStorage;
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
    public Collection<User> getFilmLikes(int filmId) {
        return userDbStorage.getFilmLikes(filmId);
    }
}
