package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Users;

public interface UserService {

    Users saveUser(Users user);

    Users findByLogin(String login);

    Users findByLoginAndPassword(String login, String password);
}
