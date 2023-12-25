package me.drpuc.lda.radio;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RadioStatus {
    private RadioMode mode;
    private RadioBand band;
    private byte rst;
}
