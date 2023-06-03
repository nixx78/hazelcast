package lv.nixx.poc.hazelcast.service;

import com.hazelcast.map.IMap;
import com.hazelcast.projection.Projection;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.PersonDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class Service implements Serializable {

    public Collection<PersonDTO> getPersons(IMap<Integer, Person> personMap) {
        return personMap.project(new Projection<Map.Entry<Integer, Person>, PersonDTO>() {
            @Override
            public PersonDTO transform(Map.Entry<Integer, Person> entry) {
                final Person value = entry.getValue();
                return new PersonDTO(entry.getKey(), value.getName(), value.getAge());
            }
        });
    }


}
