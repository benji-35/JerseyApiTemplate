package fr.kap35.api.jersey.infrastructure.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;
import fr.kap35.api.jersey.service.authentication.AuthenticationService;
import fr.kap35.api.jersey.service.authentication.dto.LoggedAccount;
import fr.kap35.api.jersey.service.authentication.exception.LoginFailedException;
import fr.kap35.api.jersey.service.repository.AuthenticationRepository;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

public class AuthenticationIntraService implements AuthenticationService {

    private static final String SECRET_KEY = "my secret key";
    private static final String ISSUER = "my issuer";


    private final AuthenticationRepository authenticationRepository;

    public AuthenticationIntraService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public LoggedAccount login(String email, char[] password) throws LoginFailedException {
        Account account;
        try {
            account = authenticationRepository.findByEmail(email);
        } catch (AccountNotFoundException ignored) {
            throw new LoginFailedException();
        }
        if (!account.isValidPassword(password))
            throw new LoginFailedException();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim("user", account.getId().toString())
                .sign(algorithm);
        return new LoggedAccount(
                token,
                account
        );
    }

    @Override
    public Account register(String email, char[] password, String name, String pseudo) throws AccountValidationException {
        Account account = new Account(email, password, name, pseudo);
        return authenticationRepository.save(account);
    }

    @Override
    public Account getAccountByToken(String token) throws AccountNotFoundException, JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(token);
        UUID id;
        try {
            id = UUID.fromString(decodedJWT.getClaim("user").asString());
        } catch (IllegalArgumentException e) {
            throw new AccountNotFoundException();
        }
        return authenticationRepository.findById(id);
    }
}
