package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.web.rest.dto.UserDto;

public interface UserService {

    Users saveUser(Users user);

    Users findByLogin(String login);

    Users findByLoginAndPassword(String login, String password);

    UserDto getUserById(Long id);

    UserDto editUser(UserDto userDto);

    void changePassword(Long id, String password);
}
