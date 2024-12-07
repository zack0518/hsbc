package org.hsbc.cache;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.*;

@Service
public class CacheTemplate {

    private final ConcurrentHashMap<String, CacheObject> cache = new ConcurrentHashMap<>();
    //缓存清除
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CacheTemplate() {
        startCleanupTask();
    }

    public void put(String key, Object value, long ttl, TimeUnit timeUnit) {
        long expiryTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        cache.put(key, new CacheObject(value, expiryTime));
    }

    public Object get(String key) {
        CacheObject cacheObject = cache.get(key);
        if (cacheObject == null || cacheObject.isExpired()) {
            cache.remove(key);
            return null;
        }
        return cacheObject.getValue();
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    private void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            for (String key : cache.keySet()) {
                CacheObject cacheObject = cache.get(key);
                if (cacheObject != null && cacheObject.isExpired()) {
                    cache.remove(key);
                }
            }
        }, 0, 10, TimeUnit.SECONDS); // Cleanup every 10 seconds
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    private static class CacheObject {
        private final Object value;
        private final long expiryTime;

        public CacheObject(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
}