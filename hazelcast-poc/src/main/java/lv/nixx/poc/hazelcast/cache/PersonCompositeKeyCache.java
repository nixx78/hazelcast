package lv.nixx.poc.hazelcast.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.model.Category;
import lv.nixx.poc.hazelcast.model.CategoryPersonTuple;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.PersonKey;

import java.util.Collection;

public class PersonCompositeKeyCache {

    private final HazelcastInstance hazelcastInstance;
    public CrudCacheOperations<PersonKey, Person> crud;


    public PersonCompositeKeyCache(HazelcastInstance hazelcastInstance) {
        this.crud = new CrudCacheOperationsImpl<>(hazelcastInstance, "person.composite");
        this.hazelcastInstance = hazelcastInstance;
    }

    public void addBulkForSelection(long selectionId, CategoryPersonTuple... tuples) {
        //FIXME Migrate to Hazelcast 4.X
//		final ILock dLock = hazelcastInstance.getLock(String.valueOf(selectionId));
//		dLock.lock();
		try {
			for (CategoryPersonTuple t : tuples) {
				PersonKey pk = new PersonKey(selectionId, t.getCategory());
				crud.add(pk, t.getPerson());
			}
		} finally {
//			dLock.unlock();
		}
    }

    public Collection<Person> getPersonsBySelection(long selectionId) {
        return crud.getValues(Predicates.newPredicateBuilder()
                .getEntryObject()
                .key()
                .get("selectionId")
                .equal(selectionId)
        );

    }

    public Collection<Person> getPersonsBySelection(Category category) {
		return crud.getValues(Predicates.newPredicateBuilder()
				.getEntryObject()
				.key()
				.get("category")
				.equal(category)
		);
    }

    public void clearAll() {
        crud.clearAll();
    }
}
