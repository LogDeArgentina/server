package me.drpuc.lda.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
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

    @OneToMany
    private Set<Qso> qsos;

    public Station(String callsign) {
        this.callsign = callsign;
    }
}
