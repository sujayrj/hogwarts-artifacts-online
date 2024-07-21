package dev.jeppu.hogwartsuser;

import dev.jeppu.hogwartsuser.converter.UserDtoToUserConverter;
import dev.jeppu.hogwartsuser.converter.UserToUserDtoConverter;
import dev.jeppu.hogwartsuser.dto.UserDTO;
import dev.jeppu.system.Result;
import dev.jeppu.system.StatusCode;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserService userService;

    @GetMapping
    public Result getAllUsers() {
        List<UserDTO> userDTOList = userService.findAllUsers().stream()
                .map(userToUserDtoConverter::convert)
                .toList();
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Find All Success", userDTOList);
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable Integer userId) {
        HogwartsUser user = userService.findUserById(userId);
        UserDTO userDTO = this.userToUserDtoConverter.convert(user);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Find User By Id Success", userDTO);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser) {
        HogwartsUser savedUser = this.userService.createUser(newHogwartsUser);
        UserDTO savedUserDTO = this.userToUserDtoConverter.convert(savedUser);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Add User", savedUserDTO);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        HogwartsUser user = this.userDtoToUserConverter.convert(userDTO);
        HogwartsUser updatedUser = this.userService.updateUser(userId, user);
        UserDTO updatedUserDTO = this.userToUserDtoConverter.convert(updatedUser);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "Update User", updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        this.userService.deleteUser(userId);
        return new Result(Boolean.TRUE, StatusCode.SUCCESS, "User Deleted", null);
    }
}
