package application.controllers;

import application.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class UserControllerTest {
    static User user;
    static UserController userController = new UserController();

    @BeforeAll
    public static void init() {
        user = new User();
        user.setBirthday(LocalDate.of(1997, 5, 29));
        user.setEmail("a@mail.ru");
        user.setLogin("login");
        user.setName("name");
    }

    @Test
    public void testEmail() {
        user.setEmail("aaa");
        userController.createUser(user);
        Assertions.assertEquals("aaa", user.getEmail(),
                "Email должен быть валидным");
        user.setEmail("");
        Assertions.assertEquals("", user.getEmail(),
                "Поле email не должно быть пустым");
    }

    @Test
    public void loginTest() {
        user.setLogin("");
        userController.createUser(user);
        Assertions.assertEquals("", user.getLogin(),
                "Поле login не должно быть пустым");
        user.setLogin(" j j");
        userController.createUser(user);
        Assertions.assertEquals(" j j", user.getLogin(),
                "Поле login не может содержать пробелы");
    }

    @Test
    public void nameTest() {
        user.setName(null);
        userController.createUser(user);
        Assertions.assertEquals(null, user.getName(),
                "Поле name не должно быть пустым");
    }

    @Test
    public void birthdayTest() {
        user.setBirthday(LocalDate.of(2022, 7, 30));
        userController.createUser(user);
        Assertions.assertEquals(LocalDate.of(2022, 7, 30), user.getBirthday(),
                "Дата рождения не должна быть больше текущей");
    }
}