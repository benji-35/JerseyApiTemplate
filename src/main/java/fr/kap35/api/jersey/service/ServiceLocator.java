package fr.kap35.api.jersey.service;

import fr.kap35.api.jersey.service.exception.ResolveException;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static ServiceLocator locator = null;
    private final Map<Class<?>, Object> locatorMap = new HashMap<>();

    public ServiceLocator() {
        locator = this;
    }

    public static ServiceLocator getInstance() {
        if (ServiceLocator.locator == null) {
            ServiceLocator.locator = new ServiceLocator();
        }
        return ServiceLocator.locator;
    }

    public static void setInstance(ServiceLocator locator) {
        ServiceLocator.locator = locator;
    }

    public <T> void register(Class<T> interfaceClass, T instance) {
        locatorMap.put(interfaceClass, instance);
    }

    public <T> T resolve(Class<T> interfaceClass) throws ResolveException {
        if (!locatorMap.containsKey(interfaceClass)) {
            throw new ResolveException();
        }
        try {
            return (T) locatorMap.get(interfaceClass);
        } catch (Exception ignored) {
            throw new ResolveException();
        }
    }
}
