package com.keldkemp.applicationpanel.web.rest;

import com.keldkemp.applicationpanel.service.UserService;
import com.keldkemp.applicationpanel.web.rest.dto.UserChangePasswordDto;
import com.keldkemp.applicationpanel.web.rest.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/{id}")
    public UserDto editUser(@RequestBody UserDto userDto) {
        return userService.editUser(userDto);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody UserChangePasswordDto userChangePasswordDto) {
        userService.changePassword(id, userChangePasswordDto);
        return ResponseEntity.ok("{}");
    }
}
