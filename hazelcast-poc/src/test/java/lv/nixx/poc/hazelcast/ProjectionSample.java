package lv.nixx.poc.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.projection.Projections;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.PersonDTO;
import lv.nixx.poc.hazelcast.model.projection.PersonDTOProj;
import lv.nixx.poc.hazelcast.service.Service;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

public class ProjectionSample {

    private HazelcastInstance hz = HazelcastTestInstance.get();

    private final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    private IMap<Integer, Person> personMap;

    @Before
    public void init() throws ParseException {
        personMap = hz.getMap("persons.map");
        personMap.clear();
        createTestData();
    }

    @Test
    public void multiAttributeProjection() {
        final Collection<Object[]> resp = personMap.project(Projections.multiAttribute("name", "age"));
        resp.forEach(t -> System.out.println(Arrays.toString(t)));
        assertNotNull(resp);
    }

    @Test
    public void customProjection() {
        final Collection<PersonDTO> dtos = personMap.project(new PersonDTOProj());
        dtos.forEach(System.out::println);
        assertNotNull(dtos);
    }

    @Test
    public void projectionAnonymousClass() {
        // В этом случае, на сторону Hazelcast передается весь Service и он должнет быть сериализованными
        final Collection<PersonDTO> dtos = new Service().getPersons(personMap);
        dtos.forEach(System.out::println);
        assertNotNull(dtos);
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

        personMap.put(8, new Person(3, "Name3", null));
    }


}
