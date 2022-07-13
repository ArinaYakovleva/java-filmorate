package application.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class User extends CommonDataModel {
    protected Set<Integer> friends = new HashSet<>();

    @Email(message = "Email должен быть валидным")
    @NotEmpty(message = "Поле email не должно быть пустым")
    private String email;

    @NotBlank(message = "Поле login не может содержать пробелы")
    @NotEmpty(message = "Поле login не должно быть пустым")
    private String login;

    private String name;

    @Past(message = "Дата рождения не должна быть больше текущей")
    private LocalDate birthday;

    public void addFriend(User friend) {
        friends.add(friend.getId());
        friend.friends.add(this.getId());
    }

    public void removeFriend(User friend) {
        friends.remove(friend.getId());
        friend.friends.remove(this.getId());
    }

    public Set<Integer> getFriends() {
        return friends;
    }
}
