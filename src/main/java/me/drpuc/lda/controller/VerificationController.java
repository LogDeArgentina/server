package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.repository.UserRepository;
import me.drpuc.lda.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/{userUuid}")
    public void verify(Authentication auth,
                         @PathVariable String userUuid) {
        User requestingUser = userService.getUserViaAuthentication(auth);
        if (!requestingUser.getRole().equals("ADMIN")) {
            throw new IllegalArgumentException("illegal access");
        }

        User user = userService.getUserByUuid(userUuid);
        user.verify();
        userRepository.save(user);
    }
}
