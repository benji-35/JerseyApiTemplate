package fr.kap35.api.jersey.domain.validation;

import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;

public class EmailValidation extends AccountValidation {

    private String email;

    public EmailValidation(String email) {
        this.email = email;
    }

    @Override
    protected void validate() throws AccountValidationException {
    }
}
