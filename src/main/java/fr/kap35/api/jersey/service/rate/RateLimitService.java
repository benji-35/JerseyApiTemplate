package fr.kap35.api.jersey.service.rate;

import java.util.Map;

public interface RateLimitService {

    boolean canExecuteResource(String client, int limit);
    Map<String, Object> resultHeader(String client, int limit);

}
