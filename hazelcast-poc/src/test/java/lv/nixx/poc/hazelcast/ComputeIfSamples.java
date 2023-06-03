package lv.nixx.poc.hazelcast;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lv.nixx.poc.hazelcast.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ComputeIfSamples {

    private HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    private String key1 = "key1";
    private IMap<String, Person> map = hazelcastInstance.getMap("person.test");

    @Before
    public void init() {
        map.clear();
    }

    @Test
    public void partialUpdateSample() {

        map.put(key1, new Person(1, "name1", new Date()));

        map.computeIfPresent(key1, (k, v) -> {
            Person p = v.toBuilder().build();
            p.setName(v.getName() + getExtension());
            return p;
        });

        EntryView<String, Person> entryView = map.getEntryView(key1);

        System.out.println(entryView);

        assertEquals(1, entryView.getVersion());
    }

    private String getExtension() {
        return ".changed:" + System.currentTimeMillis();
    }

    @Test
    public void computeIfPresentLockSample() throws InterruptedException {

        map.put(key1, new Person(1, "name1", new Date()));

        Thread thread = new Thread(() -> map.computeIfPresent(key1, (k, v) -> {

            System.out.println("Try to change the key: " + key1 + " to 'Version1' existing name: " + v.getName());

            Person p = v.toBuilder().build();
            p.setName("Version1");

            String name = Thread.currentThread().getName();
            try {
                System.out.println("Thread: " + name + " sleep");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread: " + name + " wakeUp!");
            return p;

        }), "PersonChangeThread");

        thread.start();

        TimeUnit.SECONDS.sleep(1);

        map.computeIfPresent(key1, (k, v) -> {
            System.out.println("Try to change the key: " + key1 + " to 'Version2' existing name: " + v.getName());
            Person p = v.toBuilder().build();
            p.setName("Version2");
            return p;
        });

        EntryView<String, Person> entryView1 = map.getEntryView(key1);
        System.out.println("Value is changed: " + entryView1);
        assertEquals("Version2", entryView1.getValue().getName());

        thread.join();

        EntryView<String, Person> entryView = map.getEntryView(key1);
        System.out.println("Value is final version: " + entryView);

        assertEquals("Version1", entryView.getValue().getName());
    }


}
