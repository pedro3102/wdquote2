package com.sapwd.wdquote.service.api;

import com.sapwd.wdquote.service.dto.ActionDTO;
import com.sapwd.wdquote.service.dto.ResourceDTO;

/**
 * Factory to manage multiples objects cache.
 */

public interface CacheFactory {
    /**
     * Get the Action object cache and return the default cache ("memory") if not implementation specified.
     *
     * @return The Cache service or MemoryCache by default.
     */
    CacheService<ActionDTO> getActionCache();

    /**
     * Get the Resource object cache and return the default cache ("memory") if not implementation specified.
     *
     * @return The Cache service or MemoryCache by default.
     */
    CacheService<ResourceDTO> getResourceCache();
}
