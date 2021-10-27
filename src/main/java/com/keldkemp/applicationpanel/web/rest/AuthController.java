package com.keldkemp.applicationpanel.web.rest;

import com.keldkemp.applicationpanel.config.jwt.JwtProvider;
import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.service.UserService;
import com.keldkemp.applicationpanel.web.rest.dto.AuthRequestDto;
import com.keldkemp.applicationpanel.web.rest.dto.AuthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    /*
    @PostMapping("/register")
    public String registerUser(@RequestBody AuthRequestDto registrationRequest) {
        Users user = new Users();
        user.setPassword(registrationRequest.getPassword());
        user.setLogin(registrationRequest.getLogin());
        userService.saveUser(user);
        return "OK";
    }
     */

    @PostMapping("/login")
    public AuthResponseDto auth(@RequestBody AuthRequestDto request) {
        Users userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new AuthResponseDto(token);
    }
}
