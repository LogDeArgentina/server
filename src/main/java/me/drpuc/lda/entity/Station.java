package me.drpuc.lda.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @Column(unique = true, nullable = false)
    private String callsign;

    @Column(length = 6)
    private String grid;

    @JsonIgnore
    @ManyToMany
    private final List<Qso> qsos = new LinkedList<>();

    public Station(String callsign) {
        this.callsign = callsign;
    }

    public void addQso(Qso qso) {
        this.qsos.add(qso);
    }

    public void removeQso(Qso qso) {
        this.qsos.remove(qso);
    }
}
