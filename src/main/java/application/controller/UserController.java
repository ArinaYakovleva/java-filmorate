package application.controller;

import application.model.User;
import application.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.exception.NotFoundException;
import util.exception.ValidationException;

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
        return userService.findItem(userId).orElseGet(() -> null);
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
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createItem(user)
                .orElseThrow(() -> new ValidationException("Ошибка при создании"));
        log.debug(String.format("Создание пользователя: %s", createdUser.toString()));
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        User updatedUser = userService.updateItem(user)
                .orElseThrow(() -> new ValidationException("Ошибка при обновлении"));

        log.debug(String.format("Обновление пользователя: %s", updatedUser.toString()));
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.findItem(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User friend = userService.findItem(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userService.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}")
    public User deleteUser(@PathVariable int userId) {
        return userService.deleteItem(userId).orElseThrow(() ->
                new NotFoundException("Удаляемый пользователь не найден"));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.findItem(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User friend = userService.findItem(friendId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        userService.removeFriend(user, friend);
    }
}
