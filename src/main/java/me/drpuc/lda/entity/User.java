package me.drpuc.lda.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @Column(unique = true, nullable = false)
    private String callsign;

    @OneToMany
    private List<Station> stations = new LinkedList<>();

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private boolean verified = false;

    private Date verifiedAt = null;

    @Column(nullable = false)
    private String role = "USER";

    public User(String callsign, String name, String email, String password) {
        this.callsign = callsign;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = new Date();
    }

    public void addStation(Station station) {
        this.stations.add(station);
    }

    public void verify() {
        this.verified = true;
        this.verifiedAt = new Date();
    }
}
