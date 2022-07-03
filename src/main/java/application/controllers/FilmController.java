package application.controllers;

import application.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import utils.Error;
import utils.exception.ValidationException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> filmMap = new HashMap<>();
    private int commonSize = 0;

    public void validate(Film film) {
        if (film.getName().isEmpty() || film.getName() == null) {
            throw new ValidationException("Поле name не должно быть пустым");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        }
    }

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmMap.values();
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        validate(film);
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

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<Error>> getError(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.add(new Error(error.getField(), error.getDefaultMessage()));
            log.error(String.format("Ошибка при создании/Обновлении объекта: %s", error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidateException(ValidationException e) {
        log.warn(e.getMessage());

        return ResponseEntity.internalServerError().body(Map.of("message", e.getMessage()));
    }
}
