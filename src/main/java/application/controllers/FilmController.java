package application.controllers;

import application.models.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int commonSize = 0;

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmMap.values();
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        commonSize++;
        film.setId(commonSize);
        filmMap.put(film.getId(), film);
        log.debug(String.format("Создание объекта фильма: %s", film.toString()));
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (!filmMap.containsKey(film.getId())) {
            throw new ValidationException("Фильм не найден");
        }
        filmMap.put(film.getId(), film);
        log.debug(String.format("Обновление объекта фильма: %s", film.toString()));
        return ResponseEntity.ok(film);
    }
}
