package application.controllers;

import application.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.exception.ValidationException;

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
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void loginTest() {
        user.setLogin("");
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
        user.setLogin(" j j");
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void nameTest() {
        user.setName(null);
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void birthdayTest() {
        user.setBirthday(LocalDate.of(2022, 6, 29));
        Assertions.assertThrows(ValidationException.class, () -> userController.validate(user));
    }
}