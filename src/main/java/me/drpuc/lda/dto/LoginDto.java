package me.drpuc.lda.dto;

import java.io.Serializable;

/**
 * DTO for {@link me.drpuc.lda.entity.User}
 */
public record LoginDto(String email, String password) implements Serializable { }