package utils.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank(message = "Поле login не может содержать пробелы")
@NotEmpty(message = "Поле login не должно быть пустым")
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface LoginConstraint {
    String message() default "Login is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}