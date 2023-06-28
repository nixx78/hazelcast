package lv.nixx.poc.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
