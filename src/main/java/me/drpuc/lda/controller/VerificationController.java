package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.service.VerificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {
    private final VerificationService verificationService;

    @GetMapping("/due")
    public List<User> getDueUsers() {
        return verificationService.getUnverifiedUsers();
    }

    @PostMapping("/{userUuid}")
    public void verify(@PathVariable String userUuid) {
        verificationService.verify(userUuid);
    }

    @DeleteMapping("/{userUuid}")
    public void unverify(@PathVariable String userUuid) {
        verificationService.unverify(userUuid);
    }
}
