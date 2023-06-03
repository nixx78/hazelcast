package lv.nixx.poc.hazelcast.model.projection;

import com.hazelcast.projection.Projection;
import lv.nixx.poc.hazelcast.model.Person;
import lv.nixx.poc.hazelcast.model.PersonDTO;

import java.util.Map;

public class PersonDTOProj implements Projection<Map.Entry<Integer, Person>, PersonDTO> {

    @Override
    public PersonDTO transform(Map.Entry<Integer, Person> input) {
        final Person v = input.getValue();
        return new PersonDTO(input.getKey(), v.getName(), v.getAge());
    }

}
