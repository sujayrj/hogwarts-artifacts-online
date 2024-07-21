package dev.jeppu.hogwartsuser.converter;

import dev.jeppu.hogwartsuser.HogwartsUser;
import dev.jeppu.hogwartsuser.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDTO, HogwartsUser> {
    @Override
    public HogwartsUser convert(UserDTO source) {
        HogwartsUser user = new HogwartsUser();
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setEnabled(source.enabled());
        user.setRoles(source.roles());
        return user;
    }
}
