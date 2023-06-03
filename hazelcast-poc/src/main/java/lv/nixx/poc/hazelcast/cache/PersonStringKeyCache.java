package lv.nixx.poc.hazelcast.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.model.Person;

import java.util.Collection;

public class PersonStringKeyCache {

    public CrudCacheOperations<String, Person> crud;

    public PersonStringKeyCache(HazelcastInstance hazelcastInstance) {
        this.crud = new CrudCacheOperationsImpl<>(hazelcastInstance, "person.map");
    }

    public Collection<Person> getPersonsByAttributes(Integer id, String name) {
//        return crud.getValues(Predicates.newPredicateBuilder()
//                .getEntryObject()
//                .get("id").equal(id)
//        );

        Collection<Person> values = crud.getValues(Predicates.and(Predicates.equal("id", id), Predicates.equal("name", name)));
        return values;
    }

    static class Stub extends PersonStringKeyCache {
        Stub() {
            super(null);
            crud = new CrudCacheOperationsStub<>();
        }
    }

}
