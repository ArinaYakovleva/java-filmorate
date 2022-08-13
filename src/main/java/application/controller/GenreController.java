package application.controller;

import application.model.Genre;
import application.service.IFilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final IFilmService filmService;

    @GetMapping
    Collection<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }
}
