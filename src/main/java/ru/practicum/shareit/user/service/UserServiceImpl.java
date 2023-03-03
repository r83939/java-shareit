package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl) {
        this.userRepositoryImpl = userRepositoryImpl;
    }


    @Override
    public User getUserById(long userId) {
        return userRepositoryImpl.getOne(userId);
    }

    @Override
    public List<User> getAllUsers() {

        return userRepositoryImpl.getAll();
    }

    @Override
    public User addUser(User user) throws DuplicateEmailException {
        if (userRepositoryImpl.isEmailExist(user.getEmail())){
            throw new DuplicateEmailException("этот еmail: " + user.getEmail() + " уже используется");
        }
        return userRepositoryImpl.save(user);
    }

    @Override
    public User updateUser(User user) throws  EntityNotFoundException, DuplicateEmailException {
        User updateUser = getUserById(user.getId());
        if (updateUser == null) {
            throw new EntityNotFoundException("Нет пользователя с id: " + user.getId());
        }
        if ((user.getEmail() == null || user.getEmail().isBlank())) {
            user.setEmail(updateUser.getEmail());
        }
        if ((user.getName() == null || user.getName().isBlank())) {
            user.setName(updateUser.getName());
        }
        if (!updateUser.getEmail().equals(user.getEmail()) && (getUserByEmail(user.getEmail()) != null)) {
                throw new DuplicateEmailException("этот еmail: " + user.getEmail() + " уже используется другим пользователем");
        }

        return userRepositoryImpl.save(user);
    }

    @Override
    public User deleteUser(long userId) {
        return userRepositoryImpl.delete(userId);
    }

    public User getUserByEmail(String email) {
        return userRepositoryImpl.getUserByEmail(email);
    }

}
