package fr.kap35.api.jersey.service.authentication;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;
import fr.kap35.api.jersey.service.authentication.dto.LoggedAccount;
import fr.kap35.api.jersey.service.authentication.exception.LoginFailedException;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;

public interface AuthenticationService {
    LoggedAccount login(String email, char[] password) throws LoginFailedException;
    Account register(String email, char[] password, String name, String pseudo) throws AccountValidationException;
    Account getAccountByToken(String token) throws AccountNotFoundException, JWTVerificationException;
}
