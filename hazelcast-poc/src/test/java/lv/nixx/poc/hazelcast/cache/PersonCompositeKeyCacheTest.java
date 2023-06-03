package lv.nixx.poc.hazelcast.cache;

import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Category;
import lv.nixx.poc.hazelcast.model.CategoryPersonTuple;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.PersonKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

public class PersonCompositeKeyCacheTest {

    private PersonCompositeKeyCache cache;
    private final Date date = new Date();

    @BeforeEach
    public void init() {
        cache = new PersonCompositeKeyCache(HazelcastTestInstance.get());
        cache.clearAll();
    }

    @Test
    public void crudTest() {
        cache.crud.add(new PersonKey(10L, new Category(2L, false, "RED")), new Person(102, "Name2", date));
        cache.crud.add(new PersonKey(10L, new Category(3L, false, "RED")), new Person(103, "Name3", date));

        Person p1 = new Person(101, "Name1", date);
        PersonKey key1 = new PersonKey(10L, new Category(1L, false, "RED"));

        cache.crud.add(key1, p1);

        assertEquals(3, cache.getPersonsBySelection(10L).size());

        p1.setName("Name.Changed");

        cache.crud.update(key1, p1);

        assertEquals("Name.Changed", cache.crud.get(key1).getName());

        cache.crud.remove(key1);

        assertNull(cache.crud.get(key1));
        assertEquals(2, cache.getPersonsBySelection(10L).size());
    }

    @Test
    public void getAllBySelectionIdTest() {
        cache.crud.add(new PersonKey(10L, new Category(1L, false, "RED")), new Person(101, "Name1", date));
        cache.crud.add(new PersonKey(10L, new Category(2L, false, "WHITE")), new Person(102, "Name2", date));
        cache.crud.add(new PersonKey(10L, new Category(3L, false, "RED")), new Person(103, "Name3", date));

        cache.crud.add(new PersonKey(12L, new Category(4L, false, "BLUE")), new Person(104, "Name4", date));
        cache.crud.add(new PersonKey(12L, new Category(1L, false, "RED")), new Person(105, "Name5", date));

        cache.crud.add(new PersonKey(1L, new Category(5L, false, "RED")), new Person(106, "Name5", date));

        assertAll(
                () -> assertThat(getPersonsBySelection(10L), containsInAnyOrder(101, 102, 103)),
                () -> assertThat(getPersonsBySelection(12L), containsInAnyOrder(104, 105)),
                () -> assertTrue(getPersonsBySelection(777L).isEmpty()),
                () -> assertThat(getPersonsByCategory(new Category(1L, false, "RED")), containsInAnyOrder(101, 105))
        );

    }

    @Test
    public void bulkAddTest() {
        CategoryPersonTuple[] tuples = new CategoryPersonTuple[]{
                new CategoryPersonTuple(
                        new Category(1L, true, "RED"),
                        new Person(100, "name0", date)
                ),
                new CategoryPersonTuple(
                        new Category(2L, true, "RED"),
                        new Person(101, "name1", date)
                ),
                new CategoryPersonTuple(
                        new Category(3L, true, "WHITE"),
                        new Person(102, "name2", date)
                )
        };

        long selectionId = 777L;
        cache.addBulkForSelection(selectionId, tuples);

        final Collection<Integer> personsBySelection = getPersonsBySelection(777L);
        assertThat(personsBySelection, containsInAnyOrder(100, 101, 102));

    }

    private Collection<Integer> getPersonsBySelection(long selectionId) {
        return cache.getPersonsBySelection(selectionId)
                .stream()
                .map(Person::getId)
                .collect(Collectors.toList());
    }

    private Collection<Integer> getPersonsByCategory(Category category) {
        return cache.getPersonsBySelection(category)
                .stream()
                .map(Person::getId)
                .collect(Collectors.toList());
    }

}
