package me.drpuc.lda.entity;

import jakarta.persistence.*;
import me.drpuc.lda.radio.QsoConfirmation;
import me.drpuc.lda.radio.RadioStatus;
import java.util.Date;

@Entity
public class Qso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @Column(nullable = false)
    private Date timestamp;

    @OneToOne
    private Station station;

    @Embedded
    @Column(nullable = false)
    private RadioStatus radioStatus;

    @Column(nullable = false)
    private String comment;

    @OneToOne
    private Qso confirmedQso = null;

    @Enumerated
    @Column(nullable = false)
    private QsoConfirmation confirmation = QsoConfirmation.UNCONFIRMED;
}
