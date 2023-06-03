package lv.nixx.poc.hazelcast.model.serializer;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import lv.nixx.poc.hazelcast.model.PersonDTO;

import java.io.IOException;

public class PersonDTOSerializer implements StreamSerializer<PersonDTO> {

    @Override
    public void write(ObjectDataOutput out, PersonDTO person) throws IOException {
        out.writeObject(person.getKey());
        out.writeObject(person.getName());
        out.writeObject(person.getDateOfBirth());
    }

    @Override
    public PersonDTO read(ObjectDataInput in) throws IOException {
        return new PersonDTO(in.readObject(), in.readObject(), in.readObject());
    }

    @Override
    public int getTypeId() {
        return 777;
    }

    @Override
    public void destroy() {
    }

}
