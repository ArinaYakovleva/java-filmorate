package utils.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DescriptionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DescriptionConstraint {
    String message() default "Длина описания не должна быть больше 200";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
