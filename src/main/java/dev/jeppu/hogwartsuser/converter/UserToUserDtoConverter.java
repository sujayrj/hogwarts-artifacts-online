package dev.jeppu.hogwartsuser.converter;

import dev.jeppu.hogwartsuser.HogwartsUser;
import dev.jeppu.hogwartsuser.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDTO> {
    @Override
    public UserDTO convert(HogwartsUser source) {
        return new UserDTO(source.getId(), source.getUsername(), source.getEnabled(), source.getRoles());
    }
}
