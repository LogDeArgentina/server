package me.drpuc.lda.dto.request.radio;

import java.io.Serializable;

/**
 * DTO for {@link me.drpuc.lda.entity.Station}
 */
public record StationDto(String callsign) implements Serializable { }