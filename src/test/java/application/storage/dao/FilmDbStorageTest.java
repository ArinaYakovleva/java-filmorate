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
import org.springframework.transaction.annotation.Transactional;
import util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class FilmDbStorageTest {
    private final IFilmDbStorage filmDbStorage;
    private static Film testFilm;

    @BeforeAll
    public static void init () {
        LocalDate releaseDate = LocalDate.of(2000, 4, 18);
        AgeRestriction ageRestriction = new AgeRestriction(3, "PG-13");
        Collection<Genre> genres = new ArrayList<>();
        genres.add(new Genre(2, "Драма"));

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
            Optional<Film> film = filmDbStorage.findItem(3);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 3 не найдена", exception.getMessage());
    }

    @Test
    public void createFilm() {
        Collection<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Мелодрама"));
        LocalDate releaseDate = LocalDate.of(2000, 2, 2);
        AgeRestriction restriction = new AgeRestriction(1, "G");
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
          new AgeRestriction(1, "G"),
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
                new AgeRestriction(1, "G"),
                testFilm.getGenres(),
                0
        );
        film.setId(3);
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            Optional<Film> updated = filmDbStorage.updateItem(film);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 3 не найдена", exception.getMessage());
    }

    @Test
    public void getGenres() {
        Collection<Genre> genres = filmDbStorage.getGenres();
        Assertions.assertEquals(7, genres.size());
    }

    @Test
    public void getGenreById() {
        Genre testGenre = new Genre(1, "Мелодрама");
        Optional<Genre> genre = filmDbStorage.getGenreById(1);
        Assertions.assertEquals(testGenre, genre.get());
    }

    @Test
    public void getGenreByIdNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            Optional<Genre> genre = filmDbStorage.getGenreById(100);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 100 не найдена", exception.getMessage());
    }

    @Test
    public void getRestrictions() {
        Collection<AgeRestriction> restrictions = filmDbStorage.getAgeRestrictions();
        Assertions.assertEquals(5, restrictions.size());
    }

    @Test
    public void getRestrictionById() {
        AgeRestriction testRestriction = new AgeRestriction(
          1,
          "G"
        );
        Optional<AgeRestriction> restriction = filmDbStorage.getAgeRestrictionById(1);
        Assertions.assertEquals(testRestriction, restriction.get());
    }

    @Test
    public void getRestrictionIfNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            Optional<AgeRestriction> restriction = filmDbStorage.getAgeRestrictionById(20);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 20 не найдена", exception.getMessage());
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
        Assertions.assertEquals(1, film.get().getLikesCount());
    }

    @Test
    public void dislikeFilm() {
        filmDbStorage.likeFilm(1, 1);
        Optional<Film> film = filmDbStorage.findItem(1);
        Assertions.assertEquals(1, film.get().getLikesCount());

        filmDbStorage.dislikeFilm(1, 1);
        Optional<Film> dislikedFilm = filmDbStorage.findItem(1);
        Assertions.assertEquals(0, dislikedFilm.get().getLikesCount());
    }

    @Test
    public void getMostPopularFilms() {
        filmDbStorage.likeFilm(2, 1);
        List<Film> films = new ArrayList<>(filmDbStorage.getMostPopularFilms(2));
        Assertions.assertEquals(2, films.get(0).getId());
        Assertions.assertEquals(1, films.get(1).getId());
    }
}
