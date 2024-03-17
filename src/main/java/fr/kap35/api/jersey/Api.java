package fr.kap35.api.jersey;

import fr.kap35.api.jersey.api.annotation.connected.ConnectedVerification;
import fr.kap35.api.jersey.api.annotation.rate.RateLimitFilter;
import fr.kap35.api.jersey.api.annotation.rate.RateLimitResponse;
import fr.kap35.api.jersey.api.exception.ExceptionMapper;
import fr.kap35.api.jersey.api.resource.AuthenticationResource;
import fr.kap35.api.jersey.infrastructure.authentication.AuthenticationIntraService;
import fr.kap35.api.jersey.infrastructure.rate.RateLimitIntraService;
import fr.kap35.api.jersey.infrastructure.repository.AccountRepositoryInMemory;
import fr.kap35.api.jersey.service.ServiceLocator;
import fr.kap35.api.jersey.service.authentication.AuthenticationService;
import fr.kap35.api.jersey.service.rate.RateLimitService;
import fr.kap35.api.jersey.service.repository.AccountRepository;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationFeature;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

public class Api {
    private static final String BASE_URI = "http://0.0.0.0:8080/";
    private static String CURRENT_URI = "";

    public static HttpServer startServer() {
        initializeServices();
        ResourceConfig resourceConfig = ExceptionMapper.initMapper(initResources());

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), resourceConfig);
    }

    public static void main(String[] args) {
        HttpServer server = startServer();
        String protocol = server.getListener("grizzly").isSecure() ? "https" : "http";
        String ipAddress = server.getListener("grizzly").getHost();
        int port = server.getListener("grizzly").getPort();
        CURRENT_URI = protocol + "://" + ipAddress + ":" + port + "/";
        System.out.printf("Jersey app started with endpoints available at %s%n", CURRENT_URI);
    }

    private static ResourceConfig initResources() {
        return new ResourceConfig()
                .register(RateLimitFilter.class)
                .register(ConnectedVerification.class)
                .register(ValidationFeature.class)
                .register(AuthenticationResource.class)
                .register(RateLimitResponse.class);
    }

    public static String CURRENT_URL() {
        return CURRENT_URI;
    }

    private static void initializeServices() {
        ServiceLocator serviceLocator = new ServiceLocator();
        AccountRepository repository = new AccountRepositoryInMemory();

        serviceLocator.register(AuthenticationService.class, new AuthenticationIntraService(repository));
        serviceLocator.register(RateLimitService.class, new RateLimitIntraService());
    }
}
