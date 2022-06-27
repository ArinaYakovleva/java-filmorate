package application.controllers;

import application.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import utils.Error;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> filmMap = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmMap.values();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Film> createOrUpdateFilm(@Valid @RequestBody Film film) {
        filmMap.put(film.getId(), film);
        log.debug(String.format("Обновление/Создание объекта: %s", film.toString()));
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
}
