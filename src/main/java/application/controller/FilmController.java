package application.controller;

import application.model.Film;
import application.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.exception.CreateException;
import util.exception.NotFoundException;
import util.exception.ValidationException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmService
                .findItem(filmId)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", filmId)));
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        int filmsCount = count == null ? 10 : count;
        return filmService.getMostPopularFilms(filmsCount);
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Создание фильма: %s", film.toString()));
        return filmService
                .createItem(film)
                .orElseThrow(
                        () -> new CreateException(String.format("Ошибка при создании фильма: %s", film.toString())));
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug(String.format("Обновление фильма: %s", film.toString()));
        return filmService
                .updateItem(film)
                .orElseThrow(() -> new CreateException(String
                        .format("Ошибка при обновлении фильма: %s", film.toString())));
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.dislikeFilm(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        filmService
                .deleteItem(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID %d не найден", id)));
    }
}
