package com.ninjaone.dundie_awards.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "dundie.cache")
public class CacheProperties {
    @NestedConfigurationProperty
    private List<Cache> caches;

    @Data
    public static class Cache {
        private String name;
        private int initCapacity;
        private int maxCapacity;
        private Duration ttl;
    }

}
