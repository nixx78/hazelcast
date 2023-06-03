package lv.nixx.poc.hazelcast.predicate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Person;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class PredicateSandbox {

    private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    private IMap<Integer, Person> personMap;

    @Before
    public void init() throws ParseException {
        personMap = hazelcastInstance.getMap("persons.map");
        personMap.clear();
        createTestData();
    }

    @Test
    public void predicatesForMapWithPrimitives() {
        IMap<Integer, String> m = hazelcastInstance.getMap("primitives");

        m.putAll(
                Map.of(
                        1, "Value1",
                        2, "Value2",
                        3, "Value3",
                        4, "Value4",
                        5, "NOT_VALID"
                )
        );

        assertEquals(5, m.size());

        List<String> r = new ArrayList<>(m.values(Predicates.equal("__key", 1)));
        assertEquals("Value1", r.get(0));

        List<Map.Entry<Integer, String>> r1 = new ArrayList<>(m.entrySet(Predicates.equal("this", "NOT_VALID")));
        assertEquals(Integer.valueOf(5), r1.get(0).getKey());

    }

    @Test
    public void predicateForKeyWithLike() {

        IMap<String, String> m = hazelcastInstance.getMap("mapForKeyPredicates");

        m.putAll(
                Map.of(
                        "key1.XXX", "Value1",
                        "key2.XXX", "Value2",
                        "key3.YYY", "Value3"
                )
        );
        Predicate<String, String> sql = Predicates.sql("__key LIKE '%XXX'");

        assertThat(m.values(sql), containsInAnyOrder("Value1", "Value2"));
    }

    @Test
    public void sqlPredicateTest() {
        executeFilter("state[any]==null", 2, 1, 4, 3);
        executeFilter("state[any]==st1", 5);
        executeFilter("state[any]=='st1' OR state[any]==null", 1, 2, 3, 4, 5);
        executeFilter("state[any] in (st2,st1)", 6, 5);
        executeFilter("state[any] not in (st2, st5)", 1, 2, 3, 4, 7);
        executeFilter("name like A%", 5, 6, 7);
        executeFilter("name like %B%", 5, 6);
    }

    @Test
    public void predicateTest() {
        Predicate<Integer, Person> p = Predicates.or(
                Predicates.equal("name", "ABC"),
                Predicates.equal("name", "Name1")
        );

        Predicate<Integer, Person> p1 = Predicates.and(p, Predicates.in("state[any]", "st2", "st3"));

        System.out.println("Predicate:" + p1);

        List<Person> result = personMap.entrySet(p1)
                .stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        System.out.println(result);

        assertEquals(2, result.size());
    }

    @Test
    public void entryObjectSample() {

        PredicateBuilder.EntryObject e = Predicates.newPredicateBuilder().getEntryObject();

        PredicateBuilder p = e.get("name").equal("ABC").or(e.get("name").equal("Name1"));

        Collection<Person> values = personMap.values(p);

        Collection<Integer> ids = values.stream()
                .map(Person::getId)
                .collect(Collectors.toList());

        assertThat(ids, containsInAnyOrder(1, 5, 6));

        PredicateBuilder p1 = Predicates.newPredicateBuilder().getEntryObject().get("name").in("ABC", "Name1");
        Collection<Person> values1 = personMap.values(p1);
        ids = values1.stream().map(Person::getId).collect(Collectors.toList());

        assertThat(ids, containsInAnyOrder(1, 5, 6));
    }

    private void createTestData() throws ParseException {
        personMap.put(1, new Person(1, "Name1", df.parse("06.12.1978")));
        personMap.put(2, new Person(2, "Name2", df.parse("06.12.1980")));
        personMap.put(3, new Person(3, "Name3", df.parse("06.12.2004")));

        Person person4 = new Person(4, "Name4", df.parse("06.12.2019"));
        person4.setState(Collections.emptyList());
        personMap.put(4, person4);

        Person person5 = new Person(5, "ABC", df.parse("06.12.2019"));
        person5.setState(Arrays.asList("st1", "st2", "st3"));
        personMap.put(5, person5);

        Person person6 = new Person(6, "ABC", df.parse("06.12.2019"));
        person6.setState(Arrays.asList("st2", "st3"));
        personMap.put(6, person6);

        Person person7 = new Person(7, "A_C", df.parse("06.12.2019"));
        person7.setState(Collections.singletonList("st3"));
        personMap.put(7, person7);
    }


    private void executeFilter(String sqlPredicate, Integer... expectedIds) {

        List<Integer> f = personMap.values(Predicates.sql(sqlPredicate))
                .stream()
                .map(Person::getId)
                .collect(
                        Collectors.toList()
                );

        System.out.println("Expected: " + List.of(expectedIds) + " actual: " + f);

        assertThat(f, containsInAnyOrder(expectedIds));
    }

}
