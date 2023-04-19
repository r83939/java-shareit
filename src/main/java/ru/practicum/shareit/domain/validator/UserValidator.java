package ru.practicum.shareit.domain.validator;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.model.User;

@Service
public class UserValidator {

    public void newUserValidate(User user) throws InvalidParameterException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidParameterException("поле email должно быть заполнено.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidParameterException("поле name должно быть заполнено.");
        }
    }
}
