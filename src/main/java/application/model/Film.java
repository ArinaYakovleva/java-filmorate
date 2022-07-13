package application.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import util.constraint.ReleaseDateConstraint;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends CommonDataModel {
    @NotNull(message = "Поле name не должно быть пустым")
    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
