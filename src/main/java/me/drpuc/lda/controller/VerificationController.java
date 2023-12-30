package me.drpuc.lda.controller;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.dto.response.UserResponse;
import me.drpuc.lda.dto.response.UsersResponse;
import me.drpuc.lda.service.VerificationService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {
    private final VerificationService verificationService;

    @GetMapping("/due")
    public UsersResponse getDueUsers() {
        List<UserResponse> userResponses = new LinkedList<>();
        verificationService.getUnverifiedUsers().forEach(user ->
                userResponses.add(new UserResponse(user)));
        return new UsersResponse(userResponses);
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
