package ru.practicum.shareit.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class UserValidator {

    private final UserRepository userRepo;

    @Autowired
    public UserValidator(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void newUserValidate(User user) throws InvalidParameterException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidParameterException("поле email должно быть заполнено.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidParameterException("поле name должно быть заполнено.");
        }
    }


}
