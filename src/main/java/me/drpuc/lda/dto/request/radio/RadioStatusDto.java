package me.drpuc.lda.dto.request.radio;

import me.drpuc.lda.radio.RadioBand;
import me.drpuc.lda.radio.RadioMode;

import java.io.Serializable;

/**
 * DTO for {@link me.drpuc.lda.radio.RadioStatus}
 */
public record RadioStatusDto(RadioMode mode, RadioBand band, int rst)
        implements Serializable { }