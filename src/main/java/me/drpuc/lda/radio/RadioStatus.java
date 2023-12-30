package me.drpuc.lda.radio;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.drpuc.lda.dto.request.radio.RadioStatusDto;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RadioStatus {
    @Enumerated(EnumType.STRING)
    private RadioMode mode;
    @Enumerated(EnumType.STRING)
    private RadioBand band;
    private int rst;

    public RadioStatus(RadioStatusDto radioStatus) {
        this.mode = radioStatus.mode();
        this.band = radioStatus.band();
        this.rst = radioStatus.rst();
    }

    public boolean isValidWith(RadioStatus other) {
        return this.mode == other.mode && this.band == other.band;
    }
}
