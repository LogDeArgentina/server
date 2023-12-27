package me.drpuc.lda.service;

import lombok.RequiredArgsConstructor;
import me.drpuc.lda.config.CallsignValidator;
import me.drpuc.lda.dto.radio.CreateQsoDto;
import me.drpuc.lda.dto.radio.StationDto;
import me.drpuc.lda.entity.Qso;
import me.drpuc.lda.entity.Station;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.radio.QsoConfirmation;
import me.drpuc.lda.radio.RadioStatus;
import me.drpuc.lda.repository.QsoRepository;
import me.drpuc.lda.repository.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QsoService {
    @Value("${qso.validation.threshold.minutes}")
    private short threshold;

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

        var qso = createAndProcessQso(timestamp, radioStatusEntity, fromStation, toStation, comment);

        return qso.getUuid();
    }

    /*
    * The QSO confirmation algorithm is divided in two parts:
    * 1. Check for QSO duplicates
    * 2. Check for confirmation with QSOs sent in the opposite direction
    *
    * A QSO is a duplicate if it is within -threshold and +threshold of another QSO,
    * was sent from the same station to the same station,
    * with the same RadioStatus.
    *
    * A QSO is confirmed if its timestamp is within -threshold and +threshold of the other QSO,
    * their stations are the opposite of each other (sent = received, received = sent),
    * and they have the same RadioStatus.
    *
    * A QSO is unconfirmed on any other case.
    */
    private Qso createAndProcessQso(Date timestamp, RadioStatus radioStatusEntity, Station fromStation, Station toStation, String comment) {
        Qso newQso = new Qso(timestamp, fromStation, toStation, radioStatusEntity, comment);

        var existingQsos = qsoRepository.findByTimestampIsBetween(
                subtractThresholdTo(timestamp),
                addThresholdTo(timestamp)
        );

        List<Qso> dupes = new LinkedList<>();
        List<Qso> nonDupes = new LinkedList<>();

        for (Qso qso : existingQsos) {
            if (fromStation.equals(qso.getSentFromStation())
            && toStation.equals(qso.getSentToStation())
            && radioStatusEntity.isValidWith(qso.getRadioStatus())) {
                dupes.add(qso);
            } else {
                nonDupes.add(qso);
            }
        }

        if (!dupes.isEmpty()) {
            newQso.setConfirmation(QsoConfirmation.DUPLICATE);
            newQso = qsoRepository.save(newQso);
        }

        // Written because we only want to associate each QSO once (avoiding exponential growth)
        Set<Qso> uniqueQsos = new HashSet<>();
        for (Qso dupe : dupes) {
            if (!dupe.getAssociatedQsos().isEmpty()) {
                uniqueQsos.add(dupe.getAssociatedQsos().getFirst());
            } else {
                uniqueQsos.add(dupe);
            }
        }

        for (Qso uniqueQso : uniqueQsos) {
            newQso.associate(uniqueQso);
        }

        for (Qso nonDupe : nonDupes) {
            if (!ableToConfirm(newQso, nonDupe)) {
                continue;
            }

            nonDupe.setConfirmation(QsoConfirmation.CONFIRMED);
            nonDupe = qsoRepository.save(nonDupe);

            newQso.setConfirmation(QsoConfirmation.CONFIRMED);
            newQso.associate(nonDupe);
            return qsoRepository.save(newQso);
        }

        return qsoRepository.save(newQso);
    }

    private boolean ableToConfirm(Qso newQso, Qso otherQso) {
        return newQso.getRadioStatus().isValidWith(otherQso.getRadioStatus())
                && newQso.getSentFromStation().equals(otherQso.getSentToStation())
                && newQso.getSentToStation().equals(otherQso.getSentFromStation())
                && newQso.getConfirmation().equals(QsoConfirmation.UNCONFIRMED);
    }

    private Date addThresholdTo(Date date) {
        return Date.from(date.toInstant().plus(threshold, ChronoUnit.MINUTES));
    }

    private Date subtractThresholdTo(Date date) {
        return Date.from(date.toInstant().minus(threshold, ChronoUnit.MINUTES));
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
