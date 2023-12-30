package me.drpuc.lda.dto.request.radio;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link me.drpuc.lda.entity.Qso}
 */
public record CreateQsoDto(Date timestamp, StationDto sentFromStation, StationDto sentToStation,
                           RadioStatusDto radioStatus, String comment) implements Serializable { }