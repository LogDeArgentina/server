package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.request.auth.LoginDto;
import me.drpuc.lda.dto.request.auth.RegisterDto;
import me.drpuc.lda.dto.response.TokenResponse;
import me.drpuc.lda.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterDto registerDto) {
        String token = userService.register(registerDto);
        return new TokenResponse(token);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        return new TokenResponse(token);
    }
}
