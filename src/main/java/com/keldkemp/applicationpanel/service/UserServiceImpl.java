package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.errors.AuthExceptions;
import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.repositories.UserEntityRepository;
import com.keldkemp.applicationpanel.web.rest.dto.UserChangePasswordDto;
import com.keldkemp.applicationpanel.web.rest.dto.UserDto;
import com.keldkemp.applicationpanel.web.rest.mappers.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersMapper usersMapper;

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
        throw new AuthExceptions("Введен неверный логин или пароль!");
    }

    @Override
    public UserDto getUserById(Long id) {
        return usersMapper.userDto(userEntityRepository.findById(id).get());
    }

    @Override
    public UserDto editUser(UserDto userDto) {
        Users user = usersMapper.toUsers(userDto);
        user.setPassword(userEntityRepository.getById(user.getId()).getPassword());

        return usersMapper.userDto(userEntityRepository.save(user));
    }

    @Override
    public void changePassword(Long id, UserChangePasswordDto userChangePasswordDto) {
        Users user = userEntityRepository.findById(id).get();
        if (passwordEncoder.matches(userChangePasswordDto.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(userChangePasswordDto.getNewPassword()));
            userEntityRepository.save(user);
        } else {
            throw new RuntimeException("Введен неверно старый пароль!");
        }
    }
}
