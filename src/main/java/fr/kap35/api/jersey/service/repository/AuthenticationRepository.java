package fr.kap35.api.jersey.service.repository;

import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;

import java.util.UUID;

public interface AuthenticationRepository {

    Account save(Account account);
    Account findByEmail(String email) throws AccountNotFoundException;
    Account findById(UUID id) throws AccountNotFoundException;

}
