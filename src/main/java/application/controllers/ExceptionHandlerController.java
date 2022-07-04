package application.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import utils.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public abstract class ExceptionHandlerController {
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
