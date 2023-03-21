package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;



import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) throws EntityNotFoundException {

        return userServiceImpl.getUserById(id);
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {

        return userServiceImpl.getAllUsers();
    }

    @PostMapping()
    public UserDto createUser(@RequestBody  @Valid User user) throws DuplicateEmailException, InvalidParameterException {

        return userServiceImpl.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,
                               @RequestBody @Valid User user) throws EntityNotFoundException, DuplicateEmailException {
        user.setId(id);
        return  userServiceImpl.updateUser(user);

    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) throws InvalidParameterException, EntityNotFoundException {

        return userServiceImpl.deleteUser(id);
    }
}
