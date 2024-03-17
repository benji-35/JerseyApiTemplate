package fr.kap35.api.jersey.api.annotation.rate;

import fr.kap35.api.jersey.api.annotation.connected.Connected;
import fr.kap35.api.jersey.api.dto.output.ErrorDto;
import fr.kap35.api.jersey.service.ServiceLocator;
import fr.kap35.api.jersey.service.exception.ResolveException;
import fr.kap35.api.jersey.service.rate.RateLimitService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider
public class RateLimitFilter implements ContainerRequestFilter {

    @Inject
    public ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        Method resourceMethod = resourceInfo.getResourceMethod();
        Class<?> classMethod = resourceInfo.getResourceClass();

        if (resourceMethod == null)
            return;
        if (!resourceMethod.isAnnotationPresent(RateLimit.class) && !classMethod.isAnnotationPresent(RateLimit.class)) {
            return;
        }
        RateLimit rateLimit;
        if (resourceMethod.isAnnotationPresent(RateLimit.class)) {
            rateLimit = resourceMethod.getAnnotation(RateLimit.class);
        } else {
            rateLimit = classMethod.getAnnotation(RateLimit.class);
        }

        RateLimitService rateLimitService;
        try {
            rateLimitService = ServiceLocator.getInstance().resolve(RateLimitService.class);
        } catch (ResolveException e) {
            requestContext.abortWith(Response
                    .status(Response.Status.SERVICE_UNAVAILABLE)
                            .entity(new ErrorDto(
                                    "SERVICE_UNAVAILABLE",
                                    "Rate limit service unavailable"
                            ))
                    .build());
            return;
        }
        if (!rateLimitService.canExecuteResource(
                getClientIdentifier(requestContext),
                rateLimit.limit()
        )) {
            requestContext.abortWith(Response
                    .status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(new ErrorDto(
                            "TOO_MANY_REQUESTS",
                            "You sent to many requests"
                    ))
                    .build());
            return;
        }
    }

    public static String getClientIdentifier(ContainerRequestContext requestContext) {
        return requestContext.getHeaders().getFirst("X-Forwarded-For");
    }
}
