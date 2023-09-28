package com.file.upload.demo;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.file.upload.demo")
public class Config {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("rootChildren");
    }

}
