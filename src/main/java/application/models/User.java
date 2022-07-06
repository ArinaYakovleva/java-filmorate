package application.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private int id;

    @Email(message = "Email должен быть валидным")
    @NotEmpty(message = "Поле email не должно быть пустым")
    private String email;

    @NotBlank(message = "Поле login не может содержать пробелы")
    @NotEmpty(message = "Поле login не должно быть пустым")
    private String login;

    private String name;

    @Past(message = "Дата рождения не должна быть больше текущей")
    private LocalDate birthday;
}
