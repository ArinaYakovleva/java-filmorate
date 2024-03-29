package application.storage.dao;

import application.model.AgeRestriction;
import application.model.Film;
import application.model.Genre;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class FilmDbStorageTest {
    private final IFilmDbStorage filmDbStorage;
    private static Film testFilm;

    @BeforeAll
    public static void init() {
        LocalDate releaseDate = LocalDate.of(2000, 4, 18);
        AgeRestriction ageRestriction = new AgeRestriction(3);
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(2));

        testFilm = new Film("Зеленая миля", "В тюрьме для смертников " +
                "появляется заключенный с божественным даром", releaseDate,
                189, ageRestriction, genres, 0);
    }

    @Test
    public void getAllFilms() {
        Collection<Film> films = filmDbStorage.findAll();
        Assertions.assertEquals(2, films.size());
    }

    @Test
    public void getFilmById() {
        Optional<Film> film = filmDbStorage.findItem(1);
        Assertions.assertEquals(1, film.get().getId());
        Assertions.assertEquals(testFilm, film.get());
    }

    @Test
    public void getFilmNotFoundById() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            filmDbStorage.findItem(3);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 3 не найдена", exception.getMessage());
    }

    @Test
    public void createFilm() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1));
        LocalDate releaseDate = LocalDate.of(2000, 2, 2);
        AgeRestriction restriction = new AgeRestriction(1);
        Film film = new Film(
                "Тест фильм",
                "тест описание",
                releaseDate,
                120,
                restriction,
                genres,
                0
        );
        film.setId(2);
        Optional<Film> createdFilm = filmDbStorage.createItem(film);
        Assertions.assertEquals(3, createdFilm.get().getId());
        Assertions.assertEquals(film, createdFilm.get());
    }

    @Test
    public void updateFilm() {
        Film film = new Film(
                "Обновленный",
                "обновленное",
                LocalDate.of(2000, 1, 1),
                120,
                new AgeRestriction(1),
                testFilm.getGenres(),
                0
        );
        film.setId(1);

        Optional<Film> updatedFilm = filmDbStorage.updateItem(film);
        Assertions.assertEquals(film, updatedFilm.get());
    }

    @Test
    public void updateNotFoundFilm() {
        Film film = new Film(
                "Обновленный",
                "обновленное",
                LocalDate.of(2000, 1, 1),
                120,
                new AgeRestriction(1),
                testFilm.getGenres(),
                0
        );
        film.setId(3);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            filmDbStorage.updateItem(film);
        });
    }

    @Test
    public void deleteFilm() {
        filmDbStorage.delete(1);
        Collection<Film> films = filmDbStorage.findAll();
        Assertions.assertEquals(1, films.size());
    }

    @Test
    public void likeFilm() {
        filmDbStorage.likeFilm(1, 1);
        Optional<Film> film = filmDbStorage.findItem(1);
        Assertions.assertEquals(1, film.get().getRate());
    }

    @Test
    public void dislikeFilm() {
        filmDbStorage.likeFilm(1, 1);
        Optional<Film> film = filmDbStorage.findItem(1);
        Assertions.assertEquals(1, film.get().getRate());

        filmDbStorage.dislikeFilm(1, 1);
        Optional<Film> dislikedFilm = filmDbStorage.findItem(1);
        Assertions.assertEquals(0, dislikedFilm.get().getRate());
    }

    @Test
    public void getMostPopularFilms() {
        filmDbStorage.likeFilm(2, 1);
        List<Film> films = new ArrayList<>(filmDbStorage.getMostPopularFilms(2));
        Assertions.assertEquals(2, films.get(0).getId());
        Assertions.assertEquals(1, films.get(1).getId());
    }
}
