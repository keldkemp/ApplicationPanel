package com.keldkemp.applicationpanel.web.rest;

import com.keldkemp.applicationpanel.config.jwt.JwtProvider;
import com.keldkemp.applicationpanel.errors.TokenRefreshException;
import com.keldkemp.applicationpanel.models.RefreshToken;
import com.keldkemp.applicationpanel.models.Users;
import com.keldkemp.applicationpanel.service.RefreshTokenService;
import com.keldkemp.applicationpanel.service.UserService;
import com.keldkemp.applicationpanel.web.rest.dto.AuthRequestDto;
import com.keldkemp.applicationpanel.web.rest.dto.AuthResponseDto;
import com.keldkemp.applicationpanel.web.rest.dto.RefreshTokenRequestDto;
import com.keldkemp.applicationpanel.web.rest.dto.TokenRefreshResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;

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
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());

        return new AuthResponseDto(token, refreshToken.getToken());
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtProvider.generateToken(user.getLogin());
                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
