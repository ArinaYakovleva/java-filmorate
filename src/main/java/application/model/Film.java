package application.model;

import lombok.Data;
import utils.constraints.DescriptionConstraint;
import utils.constraints.ReleaseDateConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull(message = "Поле name не должно быть пустым")
    private String name;

    @DescriptionConstraint
    private String description;

    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
