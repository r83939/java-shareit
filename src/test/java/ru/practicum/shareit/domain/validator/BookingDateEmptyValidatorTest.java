package ru.practicum.shareit.domain.validator;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class BookingDateEmptyValidatorTest {
    private BookingDateEmptyValidator bookingDateEmptyValidator = mock(BookingDateEmptyValidator.class);

    private ConstraintValidatorContext constraint = mock(ConstraintValidatorContext.class);

    @Test
    void isValid_whenDateEmpty() {
        when(bookingDateEmptyValidator.isValid(null, constraint)).thenReturn(false);

        Boolean result = bookingDateEmptyValidator.isValid(null, constraint);

        assertEquals(false, result);
    }

    @Test
    void isValid_whenDateExist() {
        LocalDateTime dateString = LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        when(bookingDateEmptyValidator.isValid(dateString, constraint)).thenReturn(true);

        Boolean result = bookingDateEmptyValidator.isValid(dateString, constraint);

        assertEquals(true, result);
    }
}