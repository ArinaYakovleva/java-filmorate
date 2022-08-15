package application.storage.dao;

import application.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class GenreStorage extends CommonDbStorage implements IGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getGenres() {
        String genresQuery = "select * from genres;";
        return jdbcTemplate.query(genresQuery, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        SqlRowSet genresQuery = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID=?", genreId);
        if (genresQuery.next()) {
            Genre genre = new Genre(genresQuery.getInt("genre_id"), genresQuery.getString("genre_name"));
            return Optional.of(genre);
        } else {
            throw getNotFoundError(genreId);
        }
    }
}
