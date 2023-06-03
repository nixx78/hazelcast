package lv.nixx.poc.hazelcast;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import lv.nixx.poc.hazelcast.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ExecuteOnSamples {

    // https://docs.hazelcast.org/docs/latest-dev/manual/html-single/index.html#entry-processor

    private HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    private String key1 = "key1";
    private IMap<String, Person> map = hazelcastInstance.getMap("person.test");
    private PersonUpdater personUpdater = new PersonUpdater(".changed");

    @Before
    public void init() {
        map.clear();
    }

    @Test
    public void executeOnExistingKeySample() {
        map.put(key1, new Person(1, "name1", new Date()));

        Person p = (Person) map.executeOnKey(key1, personUpdater);
        assertNotNull(p);
        assertEquals("name1.changed", p.getName());

        EntryView<String, Person> ew = map.getEntryView(key1);
        assertNotNull(ew);

        assertEquals("name1.changed", ew.getValue().getName());
    }

    @Test
    public void executeOnNotExistingKeySample() {
        Person p = (Person) map.executeOnKey("notExistingKey", personUpdater);
        assertNull(p);
        assertTrue(map.isEmpty());
    }

    @Test
    public void concurrencyTest() throws InterruptedException {

        map.put(key1, new Person(1, "name1", new Date()));

        PersonUpdater updaterToVersion1 = new PersonUpdater(".version1");
        PersonUpdater updaterToVersion2 = new PersonUpdater(".version2");

        Thread thread = new Thread(() -> map.executeOnKey(key1,  updaterToVersion1),"PersonChangeThread");
        thread.start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println("Before change.....");
        map.executeOnKey(key1,  updaterToVersion2);
        System.out.println("Changed....");

        thread.join();

        EntryView<String, Person> ew = map.getEntryView(key1);
        System.out.println(ew);

        assertEquals("name1.version1.version2", ew.getValue().getName());
    }

    static class PersonUpdater implements EntryProcessor<String, Person, Person> {

        private final String updateValue;

        PersonUpdater(String updateValue) {
            this.updateValue = updateValue;
        }

        @Override
        public Person process(Map.Entry<String, Person> entry) {
            // Skip Update in this case
            if (entry.getValue() == null) {
                return null;
            } else {
                Person p = entry.getValue();
                String name = p.getName();

                System.out.println(entry + " update to:" + updateValue + " current value:" + name);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {System.err.println(e.getMessage());}

                p.setName(name + updateValue);
                entry.setValue(p);
                System.out.println(entry + " updated");
                return p;
            }
        }
    }

}
