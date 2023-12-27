package me.drpuc.lda.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.drpuc.lda.radio.QsoConfirmation;
import me.drpuc.lda.radio.RadioStatus;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, updatable = false)
    private String uuid;

    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne
    private Station sentFromStation;

    @ManyToOne
    private Station sentToStation;

    @Embedded
    @Column(nullable = false)
    private RadioStatus radioStatus;

    @Column(nullable = false)
    private String comment;

    @ManyToMany
    private final List<Qso> associatedQsos = new LinkedList<>();

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QsoConfirmation confirmation = QsoConfirmation.UNCONFIRMED;

    public Qso(Date timestamp, Station from, Station to, RadioStatus radioStatus, String comment) {
        this.timestamp = timestamp;
        this.sentFromStation = from;
        this.sentToStation = to;
        this.radioStatus = radioStatus;
        this.comment = comment;
    }

    public void associate(Qso qso) {
        this.associatedQsos.add(qso);
    }
}
