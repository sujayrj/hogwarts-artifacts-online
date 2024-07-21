package dev.jeppu.hogwartsuser;

import dev.jeppu.system.exception.ObjectNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> hogwartsUsers;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.hogwartsUsers = new ArrayList<>();
        this.hogwartsUsers.add(u1);
        this.hogwartsUsers.add(u2);
        this.hogwartsUsers.add(u3);
    }

    @Test
    void testFindAllSuccess() {
        //given
        BDDMockito.given(userRepository.findAll()).willReturn(this.hogwartsUsers);
        //when
        List<HogwartsUser> allUsers = userService.findAllUsers();
        //then
        Assertions.assertThat(allUsers.size()).isEqualTo(3);
        Assertions.assertThat(allUsers.get(0).getId()).isEqualTo(1);
        Assertions.assertThat(allUsers.get(1).getId()).isEqualTo(2);
        Assertions.assertThat(allUsers.get(2).getId()).isEqualTo(3);

        BDDMockito.verify(userRepository, BDDMockito.times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        //given
        BDDMockito.given(userRepository.findById(1)).willReturn(Optional.of(this.hogwartsUsers.get(0)));
        //when
        HogwartsUser user = userService.findUserById(1);
        //then
        Assertions.assertThat(user.getId()).isEqualTo(1);
        Assertions.assertThat(user.getUsername()).isEqualTo("john");
        Assertions.assertThat(user.getEnabled()).isEqualTo(Boolean.TRUE);
        Assertions.assertThat(user.getRoles()).isEqualTo("admin user");

        BDDMockito.verify(userRepository, Mockito.times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        //given
        BDDMockito.given(userRepository.findById(5)).willReturn(Optional.empty());
        //when
        Throwable throwable = Assertions.catchThrowable(() -> userService.findUserById(5));
        Assertions.assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Could not find User with Id : 5");
    }

    @Test
    void testCreateUser() {
        //given
        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        BDDMockito.given(userRepository.save(BDDMockito.any(HogwartsUser.class))).willReturn(u3);

        //when
        HogwartsUser savedUser = userService.createUser(u3);

        //then
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(3);
        Assertions.assertThat(savedUser.getUsername()).isEqualTo("tom");
        Assertions.assertThat(savedUser.getEnabled()).isEqualTo(Boolean.FALSE);
        Assertions.assertThat(savedUser.getRoles()).isEqualTo("user");

        BDDMockito.verify(userRepository, Mockito.times(1)).save(BDDMockito.any(HogwartsUser.class));
    }

    @Test
    void updateUserSuccess() {
        //given
        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        HogwartsUser u31 = new HogwartsUser();
        u31.setUsername("tommmy");
        u31.setPassword("qwerty");
        u31.setEnabled(Boolean.TRUE);
        u31.setRoles("admin");

        BDDMockito.given(userRepository.findById(3)).willReturn(Optional.of(u3));
        BDDMockito.given(userRepository.save(BDDMockito.any(HogwartsUser.class))).willReturn(u31);

        //when
        HogwartsUser updatedUser = userService.updateUser(3, u31);

        //then
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("tommmy");
        Assertions.assertThat(updatedUser.getEnabled()).isEqualTo(Boolean.TRUE);
        Assertions.assertThat(updatedUser.getRoles()).isEqualTo("admin");

        BDDMockito.verify(userRepository, Mockito.times(1)).findById(BDDMockito.anyInt());
        BDDMockito.verify(userRepository, Mockito.times(1)).save(BDDMockito.any(HogwartsUser.class));
    }

    @Test
    void testUpdateNotFound() {
        //given
        HogwartsUser user6 = new HogwartsUser();
        user6.setId(6);
        user6.setUsername("tom");
        user6.setPassword("qwerty");
        user6.setEnabled(false);
        user6.setRoles("user");

        BDDMockito.given(userRepository.findById(6)).willReturn(Optional.empty());
        //when and then
        Throwable throwable = Assertions.catchThrowable(() -> userService.updateUser(6, user6));
        Assertions.assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Could not find User with Id : 6");
    }

    @Test
    void testDeleteSuccess(){
        HogwartsUser user6 = new HogwartsUser();
        user6.setId(6);
        user6.setUsername("tom");
        user6.setPassword("qwerty");
        user6.setEnabled(false);
        user6.setRoles("user");

        BDDMockito.given(userRepository.findById(8)).willReturn(Optional.of(user6));
        BDDMockito.willDoNothing().given(userRepository).deleteById(BDDMockito.anyInt());;

        userService.deleteUser(8);

        BDDMockito.verify(userRepository, Mockito.times(1)).findById(BDDMockito.anyInt());
        BDDMockito.verify(userRepository, Mockito.times(1)).deleteById(BDDMockito.anyInt());
    }

    @Test
    void testDeleteWhenUserNotFound(){
        BDDMockito.given(userRepository.findById(6)).willReturn(Optional.empty());
        Throwable throwable = Assertions.catchThrowable(() -> userService.deleteUser(6));
        Assertions.assertThat(throwable).isInstanceOf(ObjectNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("Could not find User with Id : 6");
    }

}