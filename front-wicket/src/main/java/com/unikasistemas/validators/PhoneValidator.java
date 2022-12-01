package com.unikasistemas.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Validates a phone number string. The number should contain only numbers 0-9, whitespace or the characters ()-+
 */
public class PhoneValidator extends PatternValidator {

    private static final long serialVersionUID = 1L;

    public PhoneValidator() {
        super(Pattern.compile("[0-9\\+\\-\\(\\)\\s]*"));
    }

}