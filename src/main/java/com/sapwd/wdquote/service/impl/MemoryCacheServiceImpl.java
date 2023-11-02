package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.service.api.CacheService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * A Memory implementation for the Cache Service.
 *
 * @param <T> The Object type to store in the Memory Cache.
 */
@Service
public class MemoryCacheServiceImpl<T> implements CacheService<T> {

    private final Logger log = LoggerFactory.getLogger(MemoryCacheServiceImpl.class);
    private String name;
    private final Map<String, T> cache = new HashMap<>();

    /**
     * Init the Memory Cache.
     */
    public void init(String name) {
        log.debug("Inited in Memory Cache Service: {}", name);
        this.name = name;
    }

    /**
     * Check if the Cache contains a key.
     *
     * @param key The Key to check.
     * @return boolean If the key is found.
     */
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    /**
     * Return an Object from Cache by Key.
     *
     * @param key The Key to find.
     * @return Optional If the key is found.
     */
    public Optional<T> get(String key) {
        log.debug("Get object from memory in ({}) for key: {}", this.name, key);
        return Optional.ofNullable(cache.get(key));
    }

    /**
     * Put an Object to Cache.
     *
     * @param key    The Key.
     * @param object The Object to store.
     * @return Optional If the Object is stored.
     */
    public Optional<T> put(String key, T object) {
        log.debug("Put object in({}) to memory for key: {}", this.name, key);
        return Optional.ofNullable(cache.put(key, object));
    }

    /**
     * Clear the Cache.
     */
    public void clear() {
        log.debug("Clear the memory cache({})", this.name);
        cache.clear();
    }
}
