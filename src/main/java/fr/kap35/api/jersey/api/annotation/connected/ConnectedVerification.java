package fr.kap35.api.jersey.api.annotation.connected;

import fr.kap35.api.jersey.api.dto.output.ErrorDto;
import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.service.ServiceLocator;
import fr.kap35.api.jersey.service.authentication.AuthenticationService;
import fr.kap35.api.jersey.service.exception.ResolveException;
import fr.kap35.api.jersey.service.repository.exception.AccountNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class ConnectedVerification implements ContainerRequestFilter {

    private static final String PREFIX_TOKEN = "Bearer ";

    @Inject
    public ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method resourceMethod = resourceInfo.getResourceMethod();

        if (!resourceMethod.isAnnotationPresent(Connected.class)) {
            return;
        }

        Connected connectedAnnotation = resourceMethod.getAnnotation(Connected.class);
        MultivaluedMap<String, String> headers = requestContext.getHeaders();
        List<String> authorizationHeaderValues = headers.get(connectedAnnotation.property());

        if (authorizationHeaderValues == null || authorizationHeaderValues.isEmpty() || authorizationHeaderValues.getFirst().isEmpty()) {
            requestContext.abortWith(Response
                    .status(connectedAnnotation.returned())
                    .build());
            return;
        }
        String token = authorizationHeaderValues.getFirst();
        if (!token.startsWith(PREFIX_TOKEN)) {
            requestContext.abortWith(Response
                    .status(connectedAnnotation.returned())
                    .build());
            return;
        }
        token = token.substring(PREFIX_TOKEN.length());

        Account account;
        try {
            AuthenticationService authenticationService = ServiceLocator.getInstance().resolve(AuthenticationService.class);
            account = authenticationService.getAccountByToken(token);
        } catch (ResolveException e) {
            requestContext.abortWith(Response
                    .status(Response.Status.SERVICE_UNAVAILABLE)
                    .build());
            return;
        } catch (AccountNotFoundException e) {
            requestContext.abortWith(Response
                    .status(connectedAnnotation.returned())
                    .build());
            return;
        }

        requestContext.setProperty("accountId", authorizationHeaderValues.getFirst());
        requestContext.setProperty("account", account);
    }
}
