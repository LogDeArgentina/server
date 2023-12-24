package me.drpuc.lda.service;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.config.CallsignValidator;
import me.drpuc.lda.dto.LoginDto;
import me.drpuc.lda.dto.RegisterDto;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.repository.UserRepository;
import me.drpuc.lda.security.jwt.Jwt;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CallsignValidator callsignValidator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final Jwt jwt;

    public String login(LoginDto dto) {
        var callsign = dto.getCallsign();
        var password = dto.getPassword();

        if (empty(callsign)) {
            throw new IllegalArgumentException("callsign is empty");
        }
        if (empty(password)) {
            throw new IllegalArgumentException("password is empty");
        }

        var authInputToken = new UsernamePasswordAuthenticationToken(callsign, password);
        authManager.authenticate(authInputToken);

        return jwt.generateToken(callsign);
    }

    public String register(RegisterDto dto) {
        var callsign = dto.getCallsign();
        var password = dto.getPassword();
        var name = dto.getName();
        var email = dto.getEmail();

        if (!validCallsign(callsign)) {
            throw new IllegalArgumentException("invalid callsign");
        }
        if (empty(password)) {
            throw new IllegalArgumentException("invalid password");
        }
        if (empty(name)) {
            throw new IllegalArgumentException("invalid name");
        }
        if (!validEmail(email)) {
            throw new IllegalArgumentException("invalid email");
        }

        if (userRepository.existsByCallsignOrEmail(callsign, email)) {
            throw new IllegalArgumentException("user already exists");
        }

        var encodedPass = passwordEncoder.encode(password);
        var user = new User(callsign, name, email, encodedPass);
        userRepository.save(user);

        return jwt.generateToken(callsign);
    }

    private boolean validCallsign(String callsign) {
        if (empty(callsign)) {
            return false;
        }

        return callsignValidator.isValid(callsign);
    }

    private boolean validEmail(String email) {
        if (empty(email)) {
            return false;
        }

        return EmailValidator.getInstance().isValid(email);
    }

    private boolean empty(String str) {
        return str == null || str.isBlank();
    }
}
