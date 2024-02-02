package lv.nixx.poc.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.util.List;

@Configuration
public class ApplicationConfig {

    @Value("${hazelcast.hosts}")
    List<String> hazelcastHosts;

    @Bean
    HazelcastInstance hazelcastInstance() {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setProperty(ClientProperty.INVOCATION_TIMEOUT_SECONDS.getName(), "30");
        clientConfig.setInstanceName("HazelcastPoc");

        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.setAddresses(hazelcastHosts);


        return com.hazelcast.client.HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    Cache<String, String> helloWorldCache() {

        // https://docs.hazelcast.com/hazelcast/5.3/jcache/setup#jcache-declarative-configuration
        //https://docs.hazelcast.com/hazelcast/5.3/jcache/icache#icache-configuration


        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        // We need this setting to enable statistics in 'Management Center'
        CompleteConfiguration<String, String> cacheConfig =
                new MutableConfiguration<String, String>()
                        .setTypes(String.class, String.class)
                        .setManagementEnabled(true)
                        .setStatisticsEnabled(true);

        // Create and get the cache.
        return cacheManager.createCache("hello_world_cache", cacheConfig);
    }

    @Bean
    Cache<String, String> xmlCache() {
        //TODO Try to create cache in XML and retrieve it using getCache() -> https://docs.hazelcast.com/hazelcast/5.3/jcache/setup

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        Iterable<String> cacheNames = cacheManager.getCacheNames();

        // Create and get the cache.
        return cacheManager.getCache("XML-ConfiguredCache");
    }

}
