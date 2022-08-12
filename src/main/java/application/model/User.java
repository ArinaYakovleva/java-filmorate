package application.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.*;

@Data
public class User extends CommonDataModel {
    protected Set<Integer> friends = new HashSet<>();

    @Email(message = "Email должен быть валидным")
    @NotEmpty(message = "Поле email не должно быть пустым")
    private final String email;

    @NotBlank(message = "Поле login не может содержать пробелы")
    @NotEmpty(message = "Поле login не должно быть пустым")
    private final String login;

    private String name;

    @Past(message = "Дата рождения не должна быть больше текущей")
    private final LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("login", login);
        userMap.put("name", name);
        userMap.put("birthday", birthday);
        return userMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(friends, user.friends) && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friends, email, login, name, birthday);
    }
}
