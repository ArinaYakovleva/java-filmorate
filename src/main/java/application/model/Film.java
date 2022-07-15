package application.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import util.constraint.ReleaseDateConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends CommonDataModel {
    protected Set<Integer> likedBy = new HashSet<>();

    @NotNull(message = "Поле name не должно быть пустым")
    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive
    private int duration;

    public void likeFilm(int id) {
        likedBy.add(id);
    }

    public void dislikeFilm(int id) {
        likedBy.remove(id);
    }

    public int getLikesCount() {
        return likedBy.size();
    }
}
