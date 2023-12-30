package me.drpuc.lda.service;

import me.drpuc.lda.dto.request.auth.LoginDto;
import me.drpuc.lda.dto.request.auth.RegisterDto;
import me.drpuc.lda.entity.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    User getUserViaAuthentication(Authentication auth);
    User getUserByEmail(String email);
    User getUserByUuid(String uuid);
    String login(LoginDto dto);
    String register(RegisterDto dto);
}
