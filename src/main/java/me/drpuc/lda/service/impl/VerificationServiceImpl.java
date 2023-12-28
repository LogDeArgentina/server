package me.drpuc.lda.service.impl;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.repository.UserRepository;
import me.drpuc.lda.service.UserService;
import me.drpuc.lda.service.VerificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final UserService userService;
    private final UserRepository userRepository;

    public List<User> getUnverifiedUsers() {
        return userRepository.findByVerified(false);
    }

    public void verify(String userUuid) {
        User user = userService.getUserByUuid(userUuid);
        user.verify();
        userRepository.save(user);
    }

    public void unverify(String userUuid) {
        User user = userService.getUserByUuid(userUuid);
        user.unverify();
        userRepository.save(user);
    }
}
