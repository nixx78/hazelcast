package lv.nixx.poc.hazelcast.predicate;

import com.hazelcast.config.AttributeConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.extractor.PersonPropertiesExtractor;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ValueExtractorSample {

    private final IMap<String, Person> map;

    public ValueExtractorSample() {
        Config cfg = new Config();
        MapConfig mapConfig = new MapConfig("map.person.extractor");
        mapConfig.addAttributeConfig(new AttributeConfig("properties", PersonPropertiesExtractor.class.getName()));
        cfg.addMapConfig(mapConfig);

        HazelcastInstance hazelcastInstance = HazelcastTestInstance.get(cfg);
        map = hazelcastInstance.getMap("map.person.extractor");
    }

    @Test
    public void requestFromMapSample() {

        Person p1 = new Person()
                .setName("Name1")
                .put("pkey1", "value1")
                .put("pkey2", "value1")
                .put("pkey3", "value3");

        Person p2 = new Person()
                .setName("Name2")
                .put("pkey1", "value1")
                .put("pkey2", "value2");

        map.put("key1", p1);
        map.put("key2", p2);

        Collection<Person> values = map.values(Predicates.sql("properties[pkey3]=value3"));
        assertEquals(1, values.size());

        System.out.println(values);

        values = map.values(Predicates.sql("properties[any]=value1"));
        assertEquals(2, values.size());

        System.out.println(values);
    }


}
