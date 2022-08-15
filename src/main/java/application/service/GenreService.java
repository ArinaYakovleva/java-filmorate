package application.service;

import application.model.Genre;
import application.storage.dao.IGenreStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.exception.NotFoundException;

import java.util.Collection;

@Service
public class GenreService implements IGenreService {
    private final IGenreStorage storage;

    @Autowired
    public GenreService(IGenreStorage storage) {
        this.storage = storage;
    }

    @Override
    public Collection<Genre> getGenres() {
        return storage.getGenres();
    }

    @Override
    public Genre getGenreById(int genreId) {
        return storage.getGenreById(genreId)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с ID %d не найден", genreId)));
    }
}
