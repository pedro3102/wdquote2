package com.sapwd.wdquote.service.impl;

import com.sapwd.wdquote.config.ApplicationProperties;
import com.sapwd.wdquote.service.api.CacheFactory;
import com.sapwd.wdquote.service.api.CacheService;
import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;
import org.springframework.stereotype.Service;

@Service
public class CacheFactoryImpl implements CacheFactory {

    public final String CACHE_REDIS = "redis";

    private final ApplicationProperties applicationProperties;

    private CacheService<ResourceDTO> resourceCache;

    public CacheFactoryImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    /**
     * Get the Action object cache and return the default cache ("memory") if not implementation specified.
     *
     * @return The Cache service or MemoryCache by default.
     */
    @Override
    public CacheService<ActionDTO> getActionCache() {
        if (CACHE_REDIS.equalsIgnoreCase(this.applicationProperties.getCache().getProvider())) {
            throw new IllegalArgumentException("Redis Cache not implemented yet.");
        }
        return buildMemoryActionCache();
    }

    /**
     * Get the Resource object cache and return the default cache ("memory") if not implementation specified.
     *
     * @return The Cache service or MemoryCache by default.
     */
    @Override
    public CacheService<ResourceDTO> getResourceCache() {
        if (CACHE_REDIS.equalsIgnoreCase(this.applicationProperties.getCache().getProvider())) {
            throw new IllegalArgumentException("Redis Cache not implemented yet.");
        }
        if (this.resourceCache == null) {
            resourceCache = buildMemoryResourceCache();
        }
        return this.resourceCache;
    }

    private CacheService<ActionDTO> buildMemoryActionCache() {
        CacheService<ActionDTO> cache = new MemoryCacheServiceImpl<>();
        cache.init("ACTIONS CACHE");
        return cache;
    }

    private CacheService<ResourceDTO> buildMemoryResourceCache() {
        CacheService<ResourceDTO> cache = new MemoryCacheServiceImpl<>();
        cache.init("RESOURCES CACHE");
        return cache;
    }
}
