package ru.practicum.shareit.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BookingDateEmptyValidator implements ConstraintValidator<BookingDateEmpty, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime dateString, ConstraintValidatorContext context) {
        if (dateString == null) {
            return false;
        }
        return true;
    }
}
