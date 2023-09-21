import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

public class HazelcastCache {

    public static Cache<String, String> helloWorldCache() {

        System.setProperty("hazelcast.jcache.provider.type", "client");

//        ClientConfig clientConfig =new ClientConfig();
//        clientConfig.getNetworkConfig().addAddress("10.0.0.180:5701");
//        clientConfig.setClusterName("DEV");
//        hz = HazelcastClient.newHazelcastClient(clientConfig);
//
        // Retrieve the CachingProvider which is automatically backed by
        // the chosen Hazelcast member or client provider. By default,
        // it returns the client-side provider.
        CachingProvider cachingProvider = Caching.getCachingProvider();

        CacheManager cacheManager = cachingProvider.getCacheManager();

        // Create a simple but typesafe configuration for the cache.
        CompleteConfiguration<String, String> config =
                new MutableConfiguration<String, String>()
                        .setTypes(String.class, String.class);

        // Create and get the cache.
        return cacheManager.createCache("hello_world_cache", config);
    }
}
