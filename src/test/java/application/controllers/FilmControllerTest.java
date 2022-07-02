package application.controllers;


import application.model.Film;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.exception.ValidationException;

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
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void testDescription() {
        film.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. " +
                "Aenean commodo ligula eget dolor. " +
                "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. " +
                "Donec quaa");
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void testReleaseDate() {
        LocalDate dateOfFirstMovie = LocalDate.of(1895, 12, 28);
        LocalDate testDate = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(dateOfFirstMovie);
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
        film.setReleaseDate(testDate);
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void testDuration() {
        film.setDuration(-1);
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
        film.setDuration(0);
        Assertions.assertThrows(ValidationException.class, () -> filmController.validate(film));
    }
}