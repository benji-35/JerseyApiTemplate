package fr.kap35.api.jersey.api.assembler;

import fr.kap35.api.jersey.api.dto.output.AccountDto;
import fr.kap35.api.jersey.domain.Account;

public class AccountAssembler {

    public static AccountDto createDto(Account account) {
        return new AccountDto(
                account.getEmail(),
                account.getUsername()
        );
    }

}
