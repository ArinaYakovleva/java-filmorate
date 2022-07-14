package application.controller;

import application.model.User;
import application.service.InMemoryUserService;
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
    private final InMemoryUserService userService;

    @Autowired
    public UserController(InMemoryUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getUsersList() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.findItem(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createItem(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateItem(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug(String.format("Добавление в друзья пользователя с ID %d у пользователя %d", friendId, id));
        User user = userService.findItem(id);
        User friend = userService.findItem(friendId);
        userService.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        userService.deleteItem(userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug(String.format("Удаление друга с ID %d у пользователя %d", friendId, id));
        User user = userService.findItem(id);
        User friend = userService.findItem(friendId);
        userService.removeFriend(user, friend);
    }
}
