package application.service;

import application.model.Film;
import application.model.User;
import application.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InMemoryFilmService extends CommonService<Film> implements FilmService {
    private final Storage<User> userStorage;

    public InMemoryFilmService(Storage<Film> storage, Storage<User> userStorage) {
        super(storage);
        this.userStorage = userStorage;
    }

    @Override
    public void likeFilm(int filmId, int userId) {
        log.debug(String.format("Лайк фильма с ID %d у пользователя %d", filmId, userId));
        User user = userStorage
                .findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));

        storage.findItem(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)))
                .likeFilm(user.getId());
    }

    @Override
    public void dislikeFilm(int filmId, int userId) {
        log.debug(String.format("Удаления лайка фильма с ID %d у пользователя %d", filmId, userId));
        User user = userStorage
                .findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));
        storage.findItem(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)))
                .dislikeFilm(user.getId());
    }

    @Override
    public Collection<Film> getMostPopularFilms(Integer count) {
        int filmsCount = count == null ? 10 : count;
        return storage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesCount, Comparator.reverseOrder()))
                .limit(filmsCount)
                .collect(Collectors.toList());
    }

}
