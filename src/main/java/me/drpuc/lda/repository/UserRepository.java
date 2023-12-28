package me.drpuc.lda.repository;

import me.drpuc.lda.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUuid(String uuid);
    List<User> findByVerified(boolean verified);
    boolean existsByCallsignOrEmail(String callsign, String email);
}
