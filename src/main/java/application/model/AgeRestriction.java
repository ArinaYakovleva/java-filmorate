package application.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AgeRestriction {
    private int id;
    private String name;

    @JsonCreator
    public AgeRestriction(int id) {
        this.id = id;
    }
}
