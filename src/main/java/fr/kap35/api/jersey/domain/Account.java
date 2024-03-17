package fr.kap35.api.jersey.domain;

import fr.kap35.api.jersey.domain.validation.AccountValidation;
import fr.kap35.api.jersey.domain.validation.EmailValidation;
import fr.kap35.api.jersey.domain.validation.PasswordValidation;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.util.UUID;

public class Account {
    private final UUID id;
    private final String email;
    private final String name;
    private final String password;
    private final String username;


    public Account(String email, char[] password, String username, String name) throws AccountValidationException {
        validate(email, password, username, name);
        this.email = email;
        this.password = hashPassword(password);
        this.name = name;
        this.username = username;
        this.id = UUID.randomUUID();
    }

    public Account(UUID id, String email, char[] password, String username, String name) throws AccountValidationException {
        validate(email, password, username, name);
        this.email = email;
        this.password = hashPassword(password);
        this.name = name;
        this.username = username;
        this.id = id;
    }

    private void validate(String email, char[] password, String username, String name) throws AccountValidationException {
        if (username == null)
            throw new AccountValidationException("username", "cannot be null");
        if (name == null)
            throw new AccountValidationException("name", "cannot be null");
        AccountValidation validation = new EmailValidation(email)
                .addNextValidation(new PasswordValidation(password));
        validation.run();
    }
    private String hashPassword(char[] password) {
        Argon2 argon2 = Argon2Factory.create();
        String hash;

        try {
            hash = argon2.hash(10, 65536, 1, password);
            if (!argon2.verify(hash, password)) {
                return hashPassword(password);
            }
        } finally {
            argon2.wipeArray(password);
        }
        return hash;
    }

    public boolean isValidPassword(char[] password) {
        Argon2 argon2 = Argon2Factory.create();
        boolean result = false;

        try {
            result = argon2.verify(this.password, password);
        } finally {
            argon2.wipeArray(password);
        }
        return result;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
