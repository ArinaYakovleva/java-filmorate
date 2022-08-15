package application.storage.dao;

import application.model.Genre;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import util.exception.NotFoundException;

import java.util.Collection;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final IGenreStorage genreStorage;

    @Test
    public void getGenres() {
        Collection<Genre> genres = genreStorage.getGenres();
        Assertions.assertEquals(6, genres.size());
    }

    @Test
    public void getGenreById() {
        Genre testGenre = new Genre(1);
        Optional<Genre> genre = genreStorage.getGenreById(1);
        Assertions.assertEquals(testGenre, genre.get());
    }

    @Test
    public void getGenreByIdNotFound() {
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            genreStorage.getGenreById(100);
        });
        Assertions.assertEquals("Ошибка доступа к записи, запись с ID 100 не найдена", exception.getMessage());
    }
}
