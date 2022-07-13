package application.service;

import application.model.Film;
import application.model.User;
import application.storage.Storage;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class FilmService extends CommonService<Film> {
    private final Storage<User> userStorage;

    public FilmService(Storage<Film> storage, Storage<User> userStorage) {
        super(storage);
        this.userStorage = userStorage;
    }

    public void likeFilm(int filmId, int userId) {
        User user = userStorage
                .findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));

        storage.findItem(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)))
                .likeFilm(user.getId());
    }

    public void dislikeFilm(int filmId, int userId) {
        User user = userStorage
                .findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));
        storage.findItem(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)))
                .dislikeFilm(user.getId());
    }

    public Collection<Film> getMostPopularFilms(int count) {
        return storage.findAll()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesCount, Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
