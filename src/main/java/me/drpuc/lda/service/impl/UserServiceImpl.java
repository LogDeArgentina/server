package me.drpuc.lda.service.impl;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.config.CallsignValidator;
import me.drpuc.lda.dto.request.auth.LoginDto;
import me.drpuc.lda.dto.request.auth.RegisterDto;
import me.drpuc.lda.entity.Station;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.repository.StationRepository;
import me.drpuc.lda.repository.UserRepository;
import me.drpuc.lda.security.jwt.Jwt;
import me.drpuc.lda.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StationRepository stationRepository;

    private final CallsignValidator callsignValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final Jwt jwt;

    public User getUserViaAuthentication(Authentication auth) {
        var email = auth.getName();
        return getUserByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found via: " + email));
    }

    public User getUserByUuid(String uuid) {
        return userRepository
                .findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("user not found via: " + uuid));
    }

    public String login(LoginDto dto) {
        var email = dto.email();
        var password = dto.password();

        var authInputToken = new UsernamePasswordAuthenticationToken(email, password);
        authManager.authenticate(authInputToken);

        return jwt.generateToken(email);
    }

    public String register(RegisterDto dto) {
        var callsign = dto.callsign();
        var password = dto.password();
        var name = dto.name();
        var email = dto.email();

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
        var station = new Station(callsign);
        stationRepository.save(station);

        user.addStation(station);
        userRepository.save(user);

        return jwt.generateToken(email);
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
