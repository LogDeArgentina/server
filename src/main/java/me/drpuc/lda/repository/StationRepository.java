package me.drpuc.lda.repository;

import me.drpuc.lda.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {

}