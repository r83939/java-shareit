package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserServiceImpl;


import javax.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl, UserMapper userMapper) {
        this.userServiceImpl = userServiceImpl;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable long id) {

        return userMapper.toUserDto(userServiceImpl.getUserById(id));
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {

        return userServiceImpl.getAllUsers().stream()
                .map(u -> userMapper.toUserDto(u))
                .collect(Collectors.toList());

    }

    @PostMapping()
    public UserDto createUser( @RequestBody  @Valid User user) throws DuplicateEmailException , InvalidParameterException{
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidParameterException("поле email должно быть заполнено.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidParameterException("поле name должно быть заполнено.");
        }
        return userMapper.toUserDto(userServiceImpl.addUser(user));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser( @PathVariable Long id,
                               @RequestBody @Valid User user) throws EntityNotFoundException, DuplicateEmailException {
        user.setId(id);
        return  userMapper.toUserDto(userServiceImpl.updateUser(user));

    }

    @DeleteMapping("/{id}")
    public UserDto deleteUser(@PathVariable Long id) throws InvalidParameterException {
        if (id <= 0) {
            throw new InvalidParameterException("id - должно быть целым числом, вы передали " +  id);
        }

        return userMapper.toUserDto(userServiceImpl.deleteUser(id));

    }

}
