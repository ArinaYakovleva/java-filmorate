package application.controller;

import application.model.Film;
import application.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilmsList() {
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmService.findItem(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getMostPopularFilms(count);
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createItem(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateItem(film);
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
        filmService.deleteItem(id);
    }
}
