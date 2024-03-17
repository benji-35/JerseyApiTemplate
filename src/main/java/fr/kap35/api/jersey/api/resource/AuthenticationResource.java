package fr.kap35.api.jersey.api.resource;

import fr.kap35.api.jersey.api.annotation.connected.Connected;
import fr.kap35.api.jersey.api.assembler.AccountAssembler;
import fr.kap35.api.jersey.api.dto.input.LoginDto;
import fr.kap35.api.jersey.api.dto.input.RegisterDto;
import fr.kap35.api.jersey.api.dto.output.LoggedInDto;
import fr.kap35.api.jersey.domain.Account;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;
import fr.kap35.api.jersey.service.ServiceLocator;
import fr.kap35.api.jersey.service.authentication.AuthenticationService;
import fr.kap35.api.jersey.service.authentication.dto.LoggedAccount;
import fr.kap35.api.jersey.service.authentication.exception.LoginFailedException;
import fr.kap35.api.jersey.service.exception.ResolveException;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Path("/register")
    @POST
    public Response register(@Valid RegisterDto body) throws AccountValidationException, ResolveException {
        AuthenticationService authenticationService = ServiceLocator.getInstance().resolve(AuthenticationService.class);
        Account account = authenticationService.register(
                body.email(),
                body.password().toCharArray(),
                body.name(),
                body.pseudo()
        );
        return Response
                .status(Response.Status.CREATED)
                .entity(AccountAssembler.createDto(account))
                .build();
    }

    @Path("/login")
    @POST
    public Response login(@Valid LoginDto body) throws ResolveException, LoginFailedException {
        AuthenticationService authenticationService = ServiceLocator.getInstance().resolve(AuthenticationService.class);
        LoggedAccount account = authenticationService.login(body.email(), body.password().toCharArray());
        return Response
                .status(Response.Status.CREATED)
                .entity(
                        new LoggedInDto(
                                account.token(),
                                AccountAssembler.createDto(account.account())
                        )
                )
                .build();
    }

    @Path("/verify")
    @POST
    @Connected
    public Response verify(@Context ContainerRequestContext reqContext) {
        // to get Account object : (Account) reqContext.getProperty("account");
        // to get Account id : (String) reqContext.getProperty("accountId");
        return Response.accepted().build();
    }

}
