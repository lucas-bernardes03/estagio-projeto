package com.unikasistemas.validators;
import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class NumberFieldValidator implements IValidator<String> {

	private final String NUMBER_PATTERN = "(^[0-9]*$)";

	private final Pattern pattern;
	private final String fieldName;

	public NumberFieldValidator(String fieldName) {
		pattern = Pattern.compile(NUMBER_PATTERN);
		this.fieldName = fieldName;
	}

	@Override
	public void validate(IValidatable<String> validatable) {

		final String number = validatable.getValue();

		// validate number
		if (pattern.matcher(number).matches() == false) {
			error(validatable, fieldName + " não é um número válido.");	
		}

	}

	private void error(IValidatable<String> validatable, String errorKey) {
		ValidationError error = new ValidationError();
        error.setMessage(errorKey);
		validatable.error(error);
	}

}