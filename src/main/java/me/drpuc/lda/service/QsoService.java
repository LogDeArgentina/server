package me.drpuc.lda.service;

import me.drpuc.lda.dto.radio.CreateQsoDto;
import me.drpuc.lda.entity.Qso;
import me.drpuc.lda.entity.User;

import java.util.List;

public interface QsoService {
    String create(User fromUser, CreateQsoDto qsoDto);
    String delete(User user, String uuid);
    Qso read(User user, String uuid);
    List<Qso> readAllFrom(User user, String stationCallsign);
}
