package com.keldkemp.applicationpanel.web.rest.mappers;

import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.web.rest.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UsersMapper {

    public List<UserDto> usersDto(List<Users> users) {
        return users.stream()
                .map(this::userDto)
                .collect(Collectors.toList());
    }

    public UserDto userDto(Users user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());

        return userDto;
    }

    @Mappings({
            @Mapping(target = "id", source =
                    "userDto.id")
    })
    public abstract Users toUsers(UserDto userDto);
}
