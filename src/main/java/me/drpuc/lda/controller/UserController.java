package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.response.UserResponse;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public UserResponse currentUser(Authentication auth) {
        User user = userService.getUserViaAuthentication(auth);
        return new UserResponse(user);
    }
}
