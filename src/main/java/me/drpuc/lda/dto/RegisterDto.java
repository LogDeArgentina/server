package me.drpuc.lda.dto;

import java.io.Serializable;

/**
 * DTO for {@link me.drpuc.lda.entity.User}
 */
public record RegisterDto(String callsign, String name, String email, String password) implements Serializable { }