package fr.kap35.api.jersey.api.dto.input;


import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull String email,
        @NotNull String password
) {}
