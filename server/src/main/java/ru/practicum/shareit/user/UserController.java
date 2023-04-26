package ru.practicum.shareit.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) throws EntityNotFoundException {

        return userService.getUserById(id);
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserDto createUser(@RequestBody  @Valid User user) throws InvalidParameterException {
        return userService.addUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id,
                               @RequestBody @Valid User user) throws EntityNotFoundException, DuplicateEmailException {
        user.setId(id);
        return  userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) throws InvalidParameterException, EntityNotFoundException {
        return userService.deleteUser(id);
    }
}
