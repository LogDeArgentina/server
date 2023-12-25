package me.drpuc.lda.repository;

import me.drpuc.lda.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, String> {
    Optional<Station> findByCallsign(String callsign);
}