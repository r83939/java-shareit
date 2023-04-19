package ru.practicum.shareit.domain.validator;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintValidatorContext;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


class UserValidatorTest  {

    UserValidator userValidator = new UserValidator();
    private ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);
    @Test
    void newUserValidate_whenEmailEmpty() {

        User user = new User(1L, "user1", null);

        assertThrows(InvalidParameterException.class, () -> userValidator.newUserValidate(user));
    }

    @Test
    void newUserValidate_whenNameEmpty() {

        User user = new User(1L, null, "user1@email.ru");

        assertThrows(InvalidParameterException.class, () -> userValidator.newUserValidate(user));
    }
}