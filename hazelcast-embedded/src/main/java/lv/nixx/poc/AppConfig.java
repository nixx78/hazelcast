package lv.nixx.poc;

import com.hazelcast.cache.HazelcastCachingProvider;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import lv.nixx.poc.service.PersonMapLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

@Configuration
@EnableSwagger2
public class AppConfig {

    @Bean
    Cache<String, String> helloWorldCache() {
        CachingProvider cachingProvider = Caching.getCachingProvider(HazelcastCachingProvider.SERVER_CACHING_PROVIDER);

        CacheManager cacheManager = cachingProvider.getCacheManager();

        // Create a simple but typesafe configuration for the cache.
        CompleteConfiguration<String, String> config =
                new MutableConfiguration<String, String>()
                        .setTypes(String.class, String.class);

        // Create and get the cache.
        return cacheManager.createCache("hello_world_cache", config);
    }

    @Bean
    public JetInstance jetInstance() {
        JetInstance jt = Jet.newJetInstance();

        PersonMapLoader pl = new PersonMapLoader();

        MapStoreConfig psc = new MapStoreConfig();
        psc.setWriteDelaySeconds(0);
        psc.setEnabled(true);
        psc.setImplementation(pl);
        psc.setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setMapStoreConfig(psc);
        mapConfig.setName("person.map");
        mapConfig.setTimeToLiveSeconds(0);

        Config hazelcastConfig = jt.getConfig().getHazelcastConfig();
        hazelcastConfig.addMapConfig(mapConfig);

        return jt;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("lv.nixx.poc"))
                .paths(PathSelectors.ant("/**/**"))
                .build();
    }


}
