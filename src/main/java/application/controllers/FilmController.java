package application.controllers;

import application.model.Film;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import utils.Error;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> filmMap = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmMap.values();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Film> createOrUpdateFilm(@Valid @RequestBody Film film) {
        filmMap.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<Error>> getError(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.add(new Error(error.getField(),error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
