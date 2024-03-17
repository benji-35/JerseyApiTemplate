package fr.kap35.api.jersey.infrastructure.rate;

import fr.kap35.api.jersey.service.rate.RateLimitService;

import java.util.HashMap;
import java.util.Map;

public class RateLimitIntraService implements RateLimitService {

    private static final long TIME_LIMIT_MS = 60000;
    private static final Map<String, Integer> requestCounts = new HashMap<>();
    private static final Map<String, Long> requestTimes = new HashMap<>();

    @Override
    public boolean canExecuteResource(String client, int limit) {
        int count = requestCounts.getOrDefault(client, 0);
        long lastRequestTime = requestTimes.getOrDefault(client, 0L);
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastRequestTime;

        if (count >= limit && timeElapsed < TIME_LIMIT_MS) {
            return false;
        } else {
            if (timeElapsed >= TIME_LIMIT_MS) {
                requestCounts.put(client, 1);
            } else {
                requestCounts.put(client, count + 1);
            }
            requestTimes.put(client, currentTime);
        }
        return true;
    }

    @Override
    public Map<String, Object> resultHeader(String client, int limit) {
        Map<String, Object> result = new HashMap<>();
        long lastRequestTime = requestTimes.getOrDefault(client, 0L);
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastRequestTime;
        result.put("rate-limit", limit);
        result.put("rate-limit-remaining", limit - requestCounts.getOrDefault(client, 1));
        result.put("rate-limit-reset", (TIME_LIMIT_MS - timeElapsed) / 1000L);
        return result;
    }
}
