package lv.nixx.poc.hazelcast.entryProcessor;

import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonServiceTest {

    private static final Logger log = LoggerFactory.getLogger(PersonServiceTest.class);

    private final PersonService service = new PersonService(HazelcastTestInstance.get());

    @BeforeEach
    public void init() {
        service.clearAll();
    }

    @Test
    public void crudOperations() {
        Long groupId = 100L;

        service.putToGroup(groupId, new Person(10, "Name.v0", new Date()));
        service.putToGroup(groupId, new Person(20, "Name.20", new Date()));
        service.putToGroup(777L, new Person(701, "Name.777", new Date()));

        log.info("After add: {}", service.log());
        assertEquals(2, service.size());

        service.putToGroup(groupId, new Person(10, "Name.v11", new Date()));

        log.info("After update: {}", service.log());

        service.removePerson(groupId, 20);
        log.info("After remove: {}", service.log());
        assertEquals(2, service.size());

        service.removePerson(groupId, 10);
        assertEquals(1, service.size());

        log.info("FinalData: {}", service.log());
    }

}
