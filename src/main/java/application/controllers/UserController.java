package application.controllers;

import application.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import utils.Error;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();

    @GetMapping
    public Collection<User> getUsersList() {
        return userMap.values();
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public User createOrUpdateUser(@Valid @RequestBody User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<Error>> getError(MethodArgumentNotValidException ex) {
        List<Error> errors = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            errors.add(new Error(error.getField(), error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
