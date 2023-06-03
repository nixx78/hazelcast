package lv.nixx.poc.hazelcast.entryProcessor;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import lv.nixx.poc.hazelcast.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO https://docs.hazelcast.com/imdg/4.2/computing/entry-processor

public class PersonService {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final IMap<Long, Set<Person>> map;

    public PersonService(HazelcastInstance hazelcastInstance) {
        this.map = hazelcastInstance.getMap("person.test");
    }

    void putToGroup(Long group, Person person) {
        map.executeOnKey(group, new PersonUpdater(person));
    }

    void removePerson(Long group, int personId) {
        map.executeOnKey(group, new PersonRemover(personId));
    }

    void clearAll() {
        map.clear();
    }

    String log() {
        return "\n" + map.entrySet()
                .stream()
                .map(t -> "GroupId:" + t.getKey() + "\n\t\t" + t.getValue().stream()
                        .map(Person::toString)
                        .collect(Collectors.joining("\n\t\t"))
                ).collect(Collectors.joining("\n"));
    }

    int size() {
        return map.size();
    }

    static class PersonUpdater implements EntryProcessor<Long, Set<Person>, Collection<Person>> {

        private final Person person;

        PersonUpdater(Person person) {
            this.person = person;
        }

        @Override
        public Collection<Person> process(Map.Entry<Long, Set<Person>> entry) {
            Set<Person> personSet = (entry.getValue() == null ? new HashSet<>() : entry.getValue());
            personSet.remove(person);
            personSet.add(person);

            entry.setValue(personSet);
            log.info("Add or Update person, id [{}] for group [{}]", person.getId(), entry.getKey());
            return personSet;
        }

    }

    static class PersonRemover implements EntryProcessor<Long, Set<Person>, Collection<Person>> {

        private final int personId;

        PersonRemover(int personId) {
            this.personId = personId;
        }

        @Override
        public Collection<Person> process(Map.Entry<Long, Set<Person>> entry) {
            Set<Person> personSet = entry.getValue();
            if (personSet != null) {
                Person o = new Person();
                o.setId(personId);
                personSet.remove(o);

                // We don't want to store empty collections
                if (personSet.isEmpty()) {
                    personSet = null;
                }
            }
            entry.setValue(personSet);
            return personSet;
        }

    }

}
