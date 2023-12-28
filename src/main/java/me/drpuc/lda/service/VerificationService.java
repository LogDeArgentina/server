package me.drpuc.lda.service;

import me.drpuc.lda.entity.User;

import java.util.List;

public interface VerificationService {
    List<User> getUnverifiedUsers();
    void verify(String userUuid);
    void unverify(String userUuid);
}
