package me.drpuc.lda.service;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.config.CallsignValidator;
import me.drpuc.lda.dto.radio.CreateQsoDto;
import me.drpuc.lda.dto.radio.StationDto;
import me.drpuc.lda.entity.Qso;
import me.drpuc.lda.entity.Station;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.radio.RadioStatus;
import me.drpuc.lda.repository.QsoRepository;
import me.drpuc.lda.repository.StationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QsoService {
    private final QsoRepository qsoRepository;
    private final StationRepository stationRepository;

    private final CallsignValidator callsignValidator;

    public String create(User fromUser, CreateQsoDto qsoDto) {
        var timestamp = qsoDto.timestamp();
        var fromStationDtoUnverified = qsoDto.sentFromStation();
        var toStationDto = qsoDto.sentToStation();
        var radioStatus = qsoDto.radioStatus();
        var comment = qsoDto.comment() == null ? "" : qsoDto.comment();

        if (invalidStationDto(fromStationDtoUnverified)
        || invalidStationDto(toStationDto)) {
            throw new IllegalArgumentException("invalid station");
        }
        if (radioStatus.rst() < 0 || radioStatus.rst() > 599) {
            throw new IllegalArgumentException("invalid rst");
        }

        var fromStation = stationRepository.findByCallsign(fromStationDtoUnverified.callsign()).orElseThrow(
                () -> new IllegalArgumentException("invalid station")
        );
        if (!fromUser.getStations().contains(fromStation)) {
            throw new IllegalArgumentException("invalid station");
        }

        var toStation = stationRepository.findByCallsign(toStationDto.callsign()).orElseGet(
                () -> stationRepository.save(new Station(toStationDto.callsign()))
        );

        var radioStatusEntity = new RadioStatus(radioStatus);
        var qso = qsoRepository.save(
                new Qso(timestamp, fromStation, toStation, radioStatusEntity, comment)
        );

        fromStation.addQso(qso);
        toStation.addQso(qso);
        stationRepository.save(fromStation);
        stationRepository.save(toStation);

        return qso.getUuid();
    }

    private boolean invalidStationDto(StationDto station) {
        if (station == null) {
            return true;
        }

        if (empty(station.callsign())) {
            return true;
        }

        return !callsignValidator.isValid(station.callsign());
    }

    private boolean empty(String str) {
        return str == null || str.isBlank();
    }
}
