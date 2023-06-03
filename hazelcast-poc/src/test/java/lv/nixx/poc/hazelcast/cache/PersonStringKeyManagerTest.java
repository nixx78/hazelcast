package lv.nixx.poc.hazelcast.cache;

import com.hazelcast.core.HazelcastInstance;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class PersonStringKeyManagerTest {
	
	private PersonStringKeyCache service;
	private final Date date = new Date();

	@BeforeEach
	public void init() {
		HazelcastInstance inst = HazelcastTestInstance.get();
		service = new PersonStringKeyCache(inst);
	}
	
	@Test
	public void crudTest() {
		String id1 = "id1";
		String id2 = "id2";
		String id100 = "id100";

		Person p102 = new Person(102, "Name2", date);
		Person p103 = new Person(103, "Name3", date);

		service.crud.add(id1, p102);
		service.crud.add(id2, p103);
		
		Person p1 = new Person(101, "Name1", date);
		
		service.crud.add(id100, p1);
		
	
		p1.setName("Name.Changed");
		
		service.crud.update(id100, p1);
		
		assertEquals("Name.Changed", service.crud.get(id100).getName());
		
		service.crud.remove(id100);
		
		assertNull(service.crud.get(id100));
		
		assertNotNull(service.crud.get(id1));
		assertNotNull(service.crud.get(id2));

	}

	@Test
	public void getAllBySelectionIdTest() {
		service.crud.add("id1", new Person(101, "Name1", date));
		service.crud.add("id2", new Person(102, "Name2", date));
		service.crud.add("id3", new Person(103, "Name3", date));
		
		service.crud.add("id4", new Person(104, "Name4", date));
		service.crud.add("id5", new Person(106, "Name5", date));
		
		service.crud.add("id6", new Person(106, "Name5", date));

		assertThat(getPersonsByAttributes(106, "Name5"), containsInAnyOrder(106, 106));

	}

	private Collection<Integer> getPersonsByAttributes(Integer id, String name) {
		return service.getPersonsByAttributes(id, name)
				.stream()
				.map(Person::getId)
				.collect(Collectors.toList());
	}
	
}
