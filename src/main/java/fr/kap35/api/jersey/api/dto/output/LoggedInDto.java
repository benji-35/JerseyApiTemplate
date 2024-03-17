package fr.kap35.api.jersey.api.dto.output;

public record LoggedInDto(
        String token,
        AccountDto account
) {}
