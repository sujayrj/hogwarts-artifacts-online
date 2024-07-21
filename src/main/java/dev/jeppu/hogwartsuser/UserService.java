package dev.jeppu.hogwartsuser;

import dev.jeppu.system.exception.ObjectNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<HogwartsUser> findAllUsers() {
        return userRepository.findAll();
    }

    public HogwartsUser findUserById(Integer userId) {
        return this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", String.valueOf(userId)));
    }

    public HogwartsUser createUser(HogwartsUser user) {
        return this.userRepository.save(user);
    }

    public HogwartsUser updateUser(Integer userId, HogwartsUser user) {
        HogwartsUser userFromDB = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", String.valueOf(userId)));
        userFromDB.setUsername(user.getUsername());
        userFromDB.setEnabled(user.getEnabled());
        userFromDB.setRoles(user.getRoles());
        return this.userRepository.save(userFromDB);
    }

    public void deleteUser(Integer userId) {
        this.userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", String.valueOf(userId)));
        this.userRepository.deleteById(userId);
    }
}
