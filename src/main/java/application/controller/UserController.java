package application.controller;

import application.model.User;
import application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.exception.CreateException;
import util.exception.NotFoundException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsersList() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService
                .findItem(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug(String.format("Создание пользователя: %s", user.toString()));
        return userService.createItem(user)
                .orElseThrow(
                        () -> new CreateException(String.format("Ошибка при создани пользователя: %s", user.toString())));
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug(String.format("Обновление пользователя: %s", user.toString()));
        return userService.updateItem(user)
                .orElseThrow(() -> new CreateException(String
                        .format("Ошибка при обновлении пользователя: %s", user.toString())));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug(String.format("Добавление в друзья пользователя с ID %d у пользователя %d", friendId, id));
        User user = userService.findItem(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с ID %d не найден", id)));
        User friend = userService.findItem(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", friendId)));
        userService.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.debug(String.format("Удаление пользователя с ID %d", userId));
        userService.deleteItem(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug(String.format("Удаление друга с ID %d у пользователя %d", friendId, id));
        User user = userService.findItem(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", id)));
        User friend = userService.findItem(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с ID %d не найден", friendId)));
        userService.removeFriend(user, friend);
    }
}
