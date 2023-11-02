package com.sapwd.wdquote.service.api;

import java.util.Optional;

public interface CacheService<T> {
    /**
     * Init the Cache.
     */
    void init(String name);

    /**
     * Check if the Cache contains a key.
     *
     * @param key The Key to check.
     * @return boolean If the key is found.
     */
    boolean containsKey(String key);

    /**
     * Return an Object from Cache by Key.
     *
     * @param key The Key to find.
     * @return Optional If the key is found.
     */
    Optional<T> get(String key);

    /**
     * Put an Object to Cache.
     *
     * @param key    The Key.
     * @param object The Object to store.
     * @return Optional If the Object is stored.
     */
    Optional<T> put(String key, T object);

    /**
     * Clear the Cache.
     */
    void clear();
}
