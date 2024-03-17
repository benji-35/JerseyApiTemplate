package fr.kap35.api.jersey.api.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;

public record RegisterDto (
        @NotNull String email,
        @NotNull String password,
        @DefaultValue("") String name,
        @NotNull String pseudo
) {}
