package lv.nixx.poc.hazelcast;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.properties.ClientProperty;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastBaseSample {

    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();

        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress("127.0.0.1:5701");

        clientConfig.setProperty(ClientProperty.INVOCATION_TIMEOUT_SECONDS.getName(), "30");

        HazelcastInstance hazelcastInstance = com.hazelcast.client.HazelcastClient.newHazelcastClient(clientConfig);
        IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");

        map.put("key", "value");
        String value = map.get("key");
        System.out.println("Value: " + value);

        hazelcastInstance.shutdown();
    }

}