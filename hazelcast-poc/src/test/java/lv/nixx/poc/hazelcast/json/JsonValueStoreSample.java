package lv.nixx.poc.hazelcast.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastJsonValue;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Account;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonValueStoreSample {

    private final HazelcastInstance hazelcast = HazelcastTestInstance.get();
    private final ObjectMapper om = new ObjectMapper();

    // Пример показывает возможность хранения объектов как Json String и фильтрации данных.
    // Также, в одной коллекции возможо храннение различных объектов, как Acount так и HazelcastJsonValue

    @Test
    public void jsonValueStoreSample() throws JsonProcessingException {
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        IMap<String, Object> map = hazelcast.getMap("json.map");

        map.putAll(Map.of(
                "id1", new AccountWrapper("id1", "current").json(),
                "id2", new AccountWrapper("id2", "deposit").json(),
                "id3", new AccountWrapper("id3", "deposit").json(),
                "id4", new HazelcastJsonValue(om.writeValueAsString(new Account("id4", "deposit")))
        ));

        map.put("id1", new AccountWrapper("id1", "current").json());

        assertEquals(4, map.size());

        Collection<Object> accByName = map.values(Predicates.equal("name", "deposit"));
        System.out.println(accByName);

        assertEquals(3, accByName.size());
    }

    class AccountWrapper extends Account implements Serializable {

        public AccountWrapper(String accountId, String name) {
            super(accountId, name);
        }

        public HazelcastJsonValue json() throws JsonProcessingException {
            return new HazelcastJsonValue(om.writeValueAsString(this));
        }

    }


}
