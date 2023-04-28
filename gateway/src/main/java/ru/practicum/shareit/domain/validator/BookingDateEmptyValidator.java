package ru.practicum.shareit.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class BookingDateEmptyValidator implements ConstraintValidator<BookingDateEmpty, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime dateString, ConstraintValidatorContext context) {
        if (dateString == null) {
            return false;
        }
        return true;
    }
}
