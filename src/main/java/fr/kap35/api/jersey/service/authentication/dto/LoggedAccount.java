package fr.kap35.api.jersey.service.authentication.dto;

import fr.kap35.api.jersey.domain.Account;

public record LoggedAccount(
        String token,
        Account account
) {
}
