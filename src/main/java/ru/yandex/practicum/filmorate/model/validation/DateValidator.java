package ru.yandex.practicum.filmorate.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.model.validation.annotation.IsAfter;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<IsAfter, LocalDate> {

    LocalDate validDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        validDate = LocalDate.parse(constraintAnnotation.current());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date.isAfter(validDate);
    }
}