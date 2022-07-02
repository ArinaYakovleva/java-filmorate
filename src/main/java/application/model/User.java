package application.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import utils.constraints.EmailConstraint;
import utils.constraints.LoginConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private int id;

    @EmailConstraint
    private String email;

    @LoginConstraint
    private String login;

    @NotNull(message = "Поле name не должно быть пустым, иначе используется login")
    private String name = login;

    @Past(message = "Дата рождения не должна быть больше текущей")
    private LocalDate birthday;
}
