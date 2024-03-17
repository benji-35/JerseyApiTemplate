package fr.kap35.api.jersey.api.exception;

import fr.kap35.api.jersey.api.dto.output.ErrorDto;
import fr.kap35.api.jersey.domain.validation.exception.AccountValidationException;
import fr.kap35.api.jersey.service.authentication.exception.LoginFailedException;
import fr.kap35.api.jersey.service.exception.ResolveException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.jersey.server.ResourceConfig;

import java.lang.reflect.InvocationTargetException;

public class ExceptionMapper {

    public static ResourceConfig initMapper(ResourceConfig config) {
        for (Class<?> exceptionMapper : ExceptionMapper.class.getClasses()) {
            if (exceptionMapper.getAnnotation(Provider.class) == null)
                continue;
            try {
                config.register(exceptionMapper.getDeclaredConstructor().newInstance());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                System.out.println("Failed to load exception " + exceptionMapper.getName());
            }
        }
        return config;
    }

    private static Response responseExceptions(Response.Status status, Object object) {
        return Response.status(status).entity(object).type("application/json").build();
    }

    @Provider
    public static class ResourceNotImplementedMapper implements jakarta.ws.rs.ext.ExceptionMapper<UnsupportedOperationException> {
        @Override
        public Response toResponse(UnsupportedOperationException exception) {
            return responseExceptions(Response.Status.NOT_IMPLEMENTED, new ErrorDto("NOT_IMPLEMENTED", "Resource is not implemented yet."));
        }
    }

    @Provider
    public static class ServiceUnavailableMapper implements jakarta.ws.rs.ext.ExceptionMapper<ResolveException> {
        @Override
        public Response toResponse(ResolveException exception) {
            return responseExceptions(Response.Status.SERVICE_UNAVAILABLE, new ErrorDto("SERVICE_UNAVAILABLE", "Restaurant service is not available."));
        }
    }

    @Provider
    public static class ConstraintViolationExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<ConstraintViolationException> {
        @Override
        public Response toResponse(ConstraintViolationException exception) {
            return responseExceptions(
                    Response.Status.BAD_REQUEST,
                    new ErrorDto("BAD_REQUEST", "1 or more parameter(s) is/are missing")
            );
        }
    }

    @Provider
    public static class AccountValidationExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<AccountValidationException> {
        @Override
        public Response toResponse(AccountValidationException exception) {
            return responseExceptions(
                    Response.Status.BAD_REQUEST,
                    new ErrorDto("BAD_REQUEST", exception.getMessage())
            );
        }
    }

    @Provider
    public static class LoginFailedExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<LoginFailedException> {
        @Override
        public Response toResponse(LoginFailedException exception) {
            return responseExceptions(
                    Response.Status.BAD_REQUEST,
                    new ErrorDto("BAD_REQUEST", "Bad email or bad password")
            );
        }
    }
}
