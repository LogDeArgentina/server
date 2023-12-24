package me.drpuc.lda.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @Column(unique = true, nullable = false)
    private String callsign;

    @Column(nullable = true)
    private String aliases = null;

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
}
