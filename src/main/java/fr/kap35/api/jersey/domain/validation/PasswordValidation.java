package fr.kap35.api.jersey.domain.validation;

import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;

public class PasswordValidation extends AccountValidation {

    private final char[] password;

    public PasswordValidation(char[] password) {
        this.password = password;
    }

    @Override
    protected void validate() throws AccountValidationException {
        if (password.length < 8)
            throw new AccountValidationException("password", "must have a minimum length of 8");
    }
}
