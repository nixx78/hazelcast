package lv.nixx.poc.service;

import com.hazelcast.map.MapLoader;
import lv.nixx.poc.model.ExternalResource;
import lv.nixx.poc.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonMapLoader implements MapLoader<String, Person> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMapLoader.class);

//    private static final Map<String, Person> externalResource = Map.of(
//            "p1", new Person().setId("p1").setName("Name1").setSurname("Surname1"),
//            "p2", new Person().setId("p2").setName("Name2").setSurname("Surname2"),
//            "p3", new Person().setId("p3").setName("Name3").setSurname("Surname3"),
//            "p4", new Person().setId("p4").setName("Name4").setSurname("Surname4"),
//            "p5", new Person().setId("p5").setName("Name5").setSurname("Surname5")
//    );

    @Override
    public Person load(String key) {
        LOG.info("Load for key [{}]", key);
        return null;
    }

    @Override
    public Map<String, Person> loadAll(Collection<String> keys) {
        LOG.info("LoadAll for keys, size [{}]", keys.size());

        return ExternalResource.data.entrySet()
                .stream()
                .filter(e -> keys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    @Override
    public Iterable<String> loadAllKeys() {
        Set<String> keys = ExternalResource.data.keySet();
        LOG.info("LoadAllKeys, size [{}]", keys.size());
        return keys;
    }

}
