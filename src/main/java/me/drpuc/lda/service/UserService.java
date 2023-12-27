package me.drpuc.lda.service;

import me.drpuc.lda.dto.LoginDto;
import me.drpuc.lda.dto.RegisterDto;
import me.drpuc.lda.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    User getUserViaAuthentication(Authentication auth);
    User getUserByEmail(String email);
    String login(LoginDto dto);
    String register(RegisterDto dto);
}
