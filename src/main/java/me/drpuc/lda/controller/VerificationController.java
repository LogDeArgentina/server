package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.service.VerificationService;
import me.drpuc.lda.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {
    private final UserService userService;
    private final VerificationService verificationService;

    @GetMapping("/due")
    public List<User> getDueUsers(Authentication auth) {
        User requestingUser = userService.getUserViaAuthentication(auth);
        if (!requestingUser.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        return verificationService.getUnverifiedUsers();
    }

    @PostMapping("/{userUuid}")
    public void verify(Authentication auth,
                         @PathVariable String userUuid) {
        User requestingUser = userService.getUserViaAuthentication(auth);
        if (!requestingUser.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        verificationService.verify(userUuid);
    }

    @DeleteMapping("/{userUuid}")
    public void unverify(Authentication auth,
                         @PathVariable String userUuid) {
        User requestingUser = userService.getUserViaAuthentication(auth);
        if (!requestingUser.getRole().equals("ADMIN")) {
            throw new AccessDeniedException("illegal access");
        }

        verificationService.unverify(userUuid);
    }
}
