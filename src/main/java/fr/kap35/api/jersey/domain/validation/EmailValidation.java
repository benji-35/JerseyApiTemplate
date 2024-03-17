package fr.kap35.api.jersey.domain.validation;

import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidation extends AccountValidation {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
    private String email;

    public EmailValidation(String email) {
        this.email = email;
    }

    @Override
    protected void validate() throws AccountValidationException {
        if (this.email == null)
            throw new AccountValidationException("email", "cannot be null");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new AccountValidationException("email", "must be an email");
        }
    }
}
