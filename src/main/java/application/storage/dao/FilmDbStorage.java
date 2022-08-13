package application.storage.dao;

import application.model.AgeRestriction;
import application.model.Film;
import application.model.Genre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import util.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements IFilmDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select film_id, name, description, release_date, duration, age.RESTRICTION_ID," +
                " age.restriction_name, (select count(user_id) from likes where film_id=f.film_id) rate " +
                "from films as f join age_restrictions as age on age.restriction_id = f.restriction_id";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> findItem(int id) {
        SqlRowSet filmsRowSet = jdbcTemplate.queryForRowSet("select film_id, name, description, release_date, " +
                "duration, age.RESTRICTION_ID, age.restriction_name, " +
                "(select count(user_id) from likes where film_id=?) rate " +
                "from films as f join age_restrictions " +
                "as age on age.restriction_id = f.restriction_id where f.film_id=?", id, id);

        Set<Genre> genres = getFilmGenreById(id);

        if (filmsRowSet.next()) {
            String name = filmsRowSet.getString("name");
            String description = filmsRowSet.getString("description");
            LocalDate releaseDate = null;
            if (filmsRowSet.getDate("release_date") != null) {
                releaseDate = filmsRowSet.getDate("release_date").toLocalDate();
            }
            int duration = filmsRowSet.getInt("duration");
            String restrictionName = filmsRowSet.getString("restriction_name");
            int restrictionId = filmsRowSet.getInt("restriction_id");
            int rate = filmsRowSet.getInt("rate");
            AgeRestriction ageRestriction = new AgeRestriction(restrictionId, restrictionName);
            Film film = new Film(name, description, releaseDate, duration, ageRestriction, genres, rate);
            film.setId(id);
            return Optional.of(film);
        } else {
            throw getNotFoundError(id);
        }
    }

    @Override
    public Optional<Film> createItem(Film item) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        SqlRowSet ageRestrictionsSet = jdbcTemplate
                .queryForRowSet("select * from AGE_RESTRICTIONS where RESTRICTION_ID=?",
                        item.getMpa().getId());


        Integer restrictionId = null;
        if (ageRestrictionsSet.next()) {
            restrictionId = ageRestrictionsSet.getInt("RESTRICTION_ID");
        }
        int generatedId = jdbcInsert.executeAndReturnKey(item.toMap(restrictionId)).intValue();
        item.setId(generatedId);

        if (item.getGenres() != null) {
            for (Genre genre : item.getGenres()) {
                try {
                    jdbcTemplate.update("insert into GENRES_OF_FILM(film_id, genre_id) VALUES (?, ?)",
                            item.getId(), genre.getId());
                } catch (DuplicateKeyException e) {
                    log.info(e.getMessage());
                }
            }
        }
        return Optional.of(item);
    }

    @Override
    public Optional<Film> updateItem(Film item) {
        String sqlQuery = "update films set name=?, description=?, release_date=?, " +
                "duration=?, restriction_id=? where film_id=?";
        String deleteCurrentGenres = "delete from genres_of_film where film_id=?";
        int result = jdbcTemplate.update(sqlQuery,
                item.getName(),
                item.getDescription(),
                item.getReleaseDate(),
                item.getDuration(),
                item.getMpa().getId(),
                item.getId()
        );
        jdbcTemplate.update(deleteCurrentGenres, item.getId());
        if (item.getGenres() != null) {
            for (Genre genre : item.getGenres()) {
                try {
                    jdbcTemplate.update("insert into genres_of_film values(?, ?);", item.getId(), genre.getId());
                } catch (DuplicateKeyException e) {
                    log.info(e.getMessage());
                }
            }
        }
        if (result == 0) {
            return Optional.empty();
        }
        return findItem(item.getId());
    }

    @Override
    public void delete(int id) {
        String deleteFilmQuery = "delete from films where film_id=?;";
        String deleteGenresQuery = "delete from genres_of_film where film_id=?;";
        try {
            jdbcTemplate.update(deleteGenresQuery, id);
            jdbcTemplate.update(deleteFilmQuery, id);
        } catch (DataAccessException e) {
            throw getNotFoundError(id);
        }
    }

    public void likeFilm(int filmId, int userId) {
        String likeFilmQuery = "insert into likes values(?, ?)";
        jdbcTemplate.update(likeFilmQuery, filmId, userId);
    }

    public void dislikeFilm(int filmId, int userId) {
        String dislikeFilmQuery = "delete from likes where FILM_ID=? and USER_ID=?";
        int result = jdbcTemplate.update(dislikeFilmQuery, filmId, userId);
        if (result == 0) {
            throw new NotFoundException(String
                    .format("Ошибка удаления лайка с фильма %d пользователя %d", filmId, userId));
        }
    }

    public Collection<Film> getMostPopularFilms(Integer count) {
        int limit = count == null ? 10 : count;
        String popularFilmsQuery = "select film_id, name, description, release_date, duration, age.RESTRICTION_ID," +
                " age.restriction_name, (select count(user_id) from likes where film_id=f.film_id) rate " +
                "from films as f join age_restrictions as age on age.restriction_id = f.restriction_id " +
                "order by rate DESC " +
                "limit ?;";

        return jdbcTemplate.query(popularFilmsQuery, (rs, rowNum) -> makeFilm(rs), limit);
    }

    public Collection<Genre> getGenres() {
        String genresQuery = "select * from genres;";
        return jdbcTemplate.query(genresQuery, (rs, rowNum) ->
                new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    public Optional<Genre> getGenreById(int genreId) {
        SqlRowSet genresQuery = jdbcTemplate.queryForRowSet("select * from GENRES where GENRE_ID=?", genreId);
        if (genresQuery.next()) {
            Genre genre = new Genre(genresQuery.getInt("genre_id"), genresQuery.getString("genre_name"));
            return Optional.of(genre);
        } else {
            throw getNotFoundError(genreId);
        }
    }

    public Collection<AgeRestriction> getAgeRestrictions() {
        String restrictionsQuery = "select * from age_restrictions";
        return jdbcTemplate.query(restrictionsQuery, (rs, rowNum) ->
                new AgeRestriction(
                        rs.getInt("restriction_id"),
                        rs.getString("restriction_name"))
        );
    }

    public Optional<AgeRestriction> getAgeRestrictionById(int id) {
        SqlRowSet restrictionsQuery = jdbcTemplate.queryForRowSet("select  * from AGE_RESTRICTIONS " +
                "where RESTRICTION_ID=?", id);
        if (restrictionsQuery.next()) {
            AgeRestriction ageRestriction = new AgeRestriction(
                    restrictionsQuery.getInt("restriction_id"),
                    restrictionsQuery.getString("restriction_name")
            );
            return Optional.of(ageRestriction);
        } else {
            throw getNotFoundError(id);
        }
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("film_id");
        String restrictionName = resultSet.getString("restriction_name");
        int restrictionId = resultSet.getInt("restriction_id");
        AgeRestriction ageRestriction = new AgeRestriction(restrictionId, restrictionName);


        Film film = new Film(
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getInt("duration"),
                ageRestriction,
                getFilmGenreById(id),
                resultSet.getInt("rate"));
        film.setId(id);
        return film;
    }

    private Set<Genre> getFilmGenreById(int id) {
        String genresQuery = "select film_id, g.genre_id, g.genre_name from genres_of_film as gf " +
                "join genres as g on g.genre_id=gf.genre_id " +
                "where film_id=?";
        SqlRowSet genresRowSet = jdbcTemplate.queryForRowSet(genresQuery, id);
        Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        while (genresRowSet.next()) {
            int genreId = genresRowSet.getInt("genre_id");
            String genreName = genresRowSet.getString("genre_name");
            genres.add(new Genre(genreId, genreName));
        }
        return genres;
    }

    private NotFoundException getNotFoundError(int recordID) {
        String errorMessage = String.format("Ошибка доступа к записи, запись с ID %d не найдена", recordID);
        log.error(errorMessage);
        return new NotFoundException(errorMessage);
    }
}
