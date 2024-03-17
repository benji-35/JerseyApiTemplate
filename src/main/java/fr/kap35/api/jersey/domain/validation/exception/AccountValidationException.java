package fr.kap35.api.jersey.domain.validation.exception;

public class AccountValidationException extends Exception{
    public AccountValidationException(String argument, String description) {
        super(argument + ": " + description);
    }
}
