package me.drpuc.lda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegexValidation {
    @Value("${regex.callsign}")
    private String callsignRegex;

    @Bean
    public CallsignValidator callsignValidator() {
        return new CallsignValidator(callsignRegex);
    }
}

