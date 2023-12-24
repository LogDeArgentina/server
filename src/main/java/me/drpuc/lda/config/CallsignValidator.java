package me.drpuc.lda.config;

import org.apache.commons.validator.routines.RegexValidator;

public class CallsignValidator extends RegexValidator {
    public CallsignValidator(String regex) {
        super(regex);
    }
}
