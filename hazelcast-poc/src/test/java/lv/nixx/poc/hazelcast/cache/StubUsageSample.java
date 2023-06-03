package lv.nixx.poc.hazelcast.cache;

import lv.nixx.poc.hazelcast.model.Person;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class StubUsageSample {

    @Test
    public void stubSample() {
        PersonStringKeyCache stub = new PersonStringKeyCache.Stub();
        stub.crud.add("key", new Person(1, "name", new Date()));

        assertEquals(1, stub.crud.size());
    }


}
