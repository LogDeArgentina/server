package me.drpuc.lda.repository;

import me.drpuc.lda.entity.Qso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface QsoRepository extends JpaRepository<Qso, String> {
    List<Qso> findByTimestampIsBetween(Date from, Date until);
    Optional<Qso> findByUuid(String uuid);
}