package application.models;

import lombok.Data;

import utils.constraints.ReleaseDateConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull(message = "Поле name не должно быть пустым")
    private String name;

    @Size(max=200)
    private String description;

    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
