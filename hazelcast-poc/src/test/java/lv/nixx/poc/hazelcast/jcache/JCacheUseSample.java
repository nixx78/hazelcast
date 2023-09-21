package lv.nixx.poc.hazelcast.jcache;

import org.junit.jupiter.api.Test;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.hazelcast.cache.HazelcastCachingProvider.MEMBER_CACHING_PROVIDER;

public class JCacheUseSample {

    @Test
    void cacheCrudSample() {

        Cache<String, String> helloWorldCache = helloWorldCache();

        helloWorldCache.put("Hello.Key", "Hello.Value");

        Iterator<Cache.Entry<String, String>> iterator = helloWorldCache.iterator();

        Map<String, String> allValues = new HashMap<>();
        while (iterator.hasNext()) {
            Cache.Entry<String, String> entry = iterator.next();
            allValues.put(entry.getKey(), entry.getValue());
        }

        System.out.println(allValues);
    }

    private Cache<String, String> helloWorldCache() {
        CachingProvider cachingProvider = Caching.getCachingProvider(MEMBER_CACHING_PROVIDER);

        CacheManager cacheManager = cachingProvider.getCacheManager();

        // Create a simple but typesafe configuration for the cache.
        CompleteConfiguration<String, String> config =
                new MutableConfiguration<String, String>()
                        .setTypes(String.class, String.class);

        // Create and get the cache.
        return cacheManager.createCache("hello_world_cache", config);
    }

}
