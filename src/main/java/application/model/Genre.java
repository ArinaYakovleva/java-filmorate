package application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Genre {
    private int id;
    private String name;

    @JsonCreator
    public Genre(int id) {
        this.id = id;
    }
}
