package com.ninjaone.dundie_awards.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.NonNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@EnableCaching
@Configuration
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager cacheManager(@NonNull CacheProperties cacheProperties) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();

        //Loop through caches and configure them
        cacheProperties.getCaches()
                .forEach(cache -> {
                    caffeineCacheManager.registerCustomCache(cache.getName(),
                            Caffeine.newBuilder()
                                    .initialCapacity(cache.getInitCapacity())
                                    .maximumSize(cache.getMaxCapacity())
                                    .expireAfterWrite(cache.getTtl())
                                    .recordStats()
                                    .buildAsync());
                });

        caffeineCacheManager.setAllowNullValues(false);
        return caffeineCacheManager;
    }
}
