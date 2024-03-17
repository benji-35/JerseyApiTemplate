package fr.kap35.api.jersey.service.repository;

import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;

import java.util.UUID;

public interface AccountRepository extends AuthenticationRepository {
    Account update(Account account) throws AccountNotFoundException;
    Account delete(UUID id) throws AccountNotFoundException;
    Account findById(UUID id) throws AccountNotFoundException;
    Account save(Account account);

}
