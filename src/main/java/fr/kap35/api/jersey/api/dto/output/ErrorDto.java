package fr.kap35.api.jersey.api.dto.output;

public record ErrorDto(
        String error,
        String description
) {}
