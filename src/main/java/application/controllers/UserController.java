package application.controllers;

import application.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utils.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends ExceptionHandlerController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int commonSize = 0;

    @GetMapping
    public Collection<User> getUsersList() {
        return userMap.values();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        commonSize++;
        user.setId(commonSize);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.debug(String.format("Создание объекта юзера: %s", user.toString()));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (!userMap.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не найден");
        }
        userMap.put(user.getId(), user);
        log.debug(String.format("Обновление объекта юзера: %s", user.toString()));
        return ResponseEntity.ok(user);
    }

}
