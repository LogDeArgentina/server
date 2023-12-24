package me.drpuc.lda.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String callsign;
    private String password;
    private String email;
    private String name;
}