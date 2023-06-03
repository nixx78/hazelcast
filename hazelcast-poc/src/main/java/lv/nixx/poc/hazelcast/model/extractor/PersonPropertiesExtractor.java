package lv.nixx.poc.hazelcast.model.extractor;

import com.hazelcast.query.extractor.ValueCollector;
import com.hazelcast.query.extractor.ValueExtractor;
import lv.nixx.poc.hazelcast.model.Person;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PersonPropertiesExtractor implements ValueExtractor<Person, String> {

    @Override
    public void extract(Person person, String arg, ValueCollector collector) {
        final Map<String, String> properties = person.getProperties();
        if ("any".equalsIgnoreCase(arg)) {
            // To avoid duplicates in values
            Set<String> v = new HashSet<>(properties.values());
            v.forEach(collector::addObject);
        } else {
            collector.addObject(properties.get(arg));
        }
    }

}
