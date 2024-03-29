package application.model;

import lombok.Data;
import util.constraint.ReleaseDateConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Data
public class Film extends CommonDataModel {
    @NotNull(message = "Поле name не должно быть пустым")
    @NotEmpty
    private final String name;

    @Size(max = 200)
    private final String description;

    @ReleaseDateConstraint
    private final LocalDate releaseDate;

    @Positive
    private final int duration;

    private final AgeRestriction mpa;

    private final Set<Genre> genres;

    private final int rate;

    public Map<String, Object> toMap(Integer restrictionId) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("release_date", releaseDate);
        map.put("duration", duration);
        map.put("restriction_id", restrictionId);
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && rate == film.rate && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(mpa, film.mpa) && Objects.equals(genres, film.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, releaseDate, duration, mpa, genres, rate);
    }
}
