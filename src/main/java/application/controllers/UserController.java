package application.controllers;

import application.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import utils.Error;
import utils.exception.ValidationException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int commonSize = 0;

    public void validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail()
                .matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$")) {
            throw new ValidationException("Email должен быть валидным\"");
        }
    }

    @GetMapping
    public Collection<User> getUsersList() {
        return userMap.values();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        commonSize++;
        user.setId(commonSize);
        userMap.put(user.getId(), user);
        log.debug(String.format("Создание объекта юзера: %s", user.toString()));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (!userMap.containsKey(user.getId())) {
            commonSize++;
            user.setId(commonSize);
        }
        userMap.put(user.getId(), user);
        log.debug(String.format("Обновление объекта юзера: %s", user.toString()));
        return ResponseEntity.ok(user);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<Error>> getError(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.add(new Error(error.getField(), error.getDefaultMessage()));
            log.error(String.format("Ошибка при создании/Обновлении объекта: %s", error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
