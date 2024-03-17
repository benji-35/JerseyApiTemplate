package fr.kap35.api.jersey.infrastructure.repository;

import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.service.repository.AccountRepository;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountRepositoryInMemory implements AccountRepository {

    Map<UUID, Account> accountMap = new HashMap<>();

    @Override
    public Account update(Account account) throws AccountNotFoundException {
        if (!accountMap.containsKey(account.getId()))
            throw new AccountNotFoundException();
        accountMap.put(account.getId(), account);
        return account;
    }

    @Override
    public Account delete(UUID id) throws AccountNotFoundException {
        if (!accountMap.containsKey(id))
            throw new AccountNotFoundException();
        return accountMap.remove(id);
    }

    @Override
    public Account findById(UUID id) throws AccountNotFoundException {
        if (!accountMap.containsKey(id))
            throw new AccountNotFoundException();
        return accountMap.get(id);
    }

    @Override
    public Account save(Account account) {
        accountMap.put(account.getId(), account);
        return account;
    }

    @Override
    public Account findByEmail(String email) throws AccountNotFoundException {
        for (Map.Entry<UUID, Account> entry : accountMap.entrySet()) {
            if (entry.getValue().getEmail().equals(email))
                return entry.getValue();
        }
        throw new AccountNotFoundException();
    }
}
