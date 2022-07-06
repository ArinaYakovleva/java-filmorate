package utils.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    @Override
    public void initialize(ReleaseDateConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return date != null && (date.isAfter(minDate) || date.isEqual(minDate));
    }
}
