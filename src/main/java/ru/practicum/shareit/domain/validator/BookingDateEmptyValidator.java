package ru.practicum.shareit.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BookingDateEmptyValidator implements ConstraintValidator<BookingDateEmpty, Date> {
    @Override
    public boolean isValid(Date dateString, ConstraintValidatorContext context) {
        if (dateString == null) {
            return false;
        }
        return true;
    }
}
