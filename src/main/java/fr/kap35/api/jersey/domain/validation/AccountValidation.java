package fr.kap35.api.jersey.domain.validation;

import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;

public abstract class AccountValidation {

    private AccountValidation nextValidation = null;

    public void run() throws AccountValidationException {
        validate();
        if (nextValidation != null)
            nextValidation.run();
    }

    protected abstract void validate() throws AccountValidationException;

    public AccountValidation addNextValidation(AccountValidation validation) {
        if (nextValidation == null) {
            nextValidation = validation;
        } else {
            nextValidation.addNextValidation(validation);
        }
        return this;
    }

}
