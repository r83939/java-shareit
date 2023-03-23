package ru.practicum.shareit.domain.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = BookingDateEmptyValidator.class)
@Documented
public @interface BookingDateEmpty {
    String message() default "Дата бронирования должна быть указана";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
