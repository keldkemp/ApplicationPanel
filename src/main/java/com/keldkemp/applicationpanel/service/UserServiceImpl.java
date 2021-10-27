package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userEntityRepository.save(user);
    }

    @Override
    public Users findByLogin(String login) {
        return userEntityRepository.findByLogin(login);
    }

    @Override
    public Users findByLoginAndPassword(String login, String password) {
        Users userEntity = findByLogin(login);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }
}
