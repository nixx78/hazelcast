package lv.nixx.poc.hazelcast;

import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EntryViewSample {

    private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    @Test
    public void getEntryViewSample() throws InterruptedException {
        String key = "key";

        IMap<String, String> map = hazelcastInstance.getMap("test.map");
        map.put(key, "value1");

        TimeUnit.SECONDS.sleep(1);

        map.put(key, "value2");

        TimeUnit.SECONDS.sleep(1);

        map.get(key);

        EntryView<String, String> entryView = map.getEntryView(key);

        System.out.println(entryView);

        assertEquals(key, entryView.getKey());
        assertEquals("value2", entryView.getValue());
        assertEquals(1, entryView.getVersion());

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

        System.out.println("Creation time:\t\t" + df.format(new Date(entryView.getCreationTime())));
        System.out.println("Update time:\t\t" + df.format(new Date(entryView.getLastUpdateTime())));
        System.out.println("Last Access time:\t" + df.format(new Date(entryView.getLastAccessTime())));
    }

}
