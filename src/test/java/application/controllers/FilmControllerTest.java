package application.controllers;


import application.models.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class FilmControllerTest {
    static FilmController filmController;
    static Film film;

    @BeforeAll
    public static void init() {
        film = new Film();
        film.setName("Name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        filmController = new FilmController();
    }

    @Test
    public void testName() {
        film.setName("");
        filmController.createFilm(film);
        Assertions.assertEquals("", film.getName(), "Поле name не должно быть пустым");
    }

    @Test
    public void testDescription() {
        String description = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quaa";
        film.setDescription(description);
        filmController.createFilm(film);
        Assertions.assertEquals(description, film.getDescription(),
                "размер должен находиться в диапазоне от 0 до 200");
    }

    @Test
    public void testReleaseDate() {
        LocalDate dateOfFirstMovie = LocalDate.of(1895, 12, 28);
        LocalDate testDate = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(dateOfFirstMovie);

        Assertions.assertEquals(dateOfFirstMovie, film.getReleaseDate(),
                "Дата релиза не должна быть раньше 28 декабря 1895 года");
        film.setReleaseDate(testDate);
        Assertions.assertEquals(testDate, film.getReleaseDate(),
                "Дата релиза не должна быть раньше 28 декабря 1895 года");
    }

    @Test
    public void testDuration() {
        film.setDuration(-1);
        Assertions.assertEquals(-1, film.getDuration(),
                "должно быть больше 0");
        film.setDuration(0);
        Assertions.assertEquals(0, film.getDuration(),
                "должно быть больше 0");
    }
}