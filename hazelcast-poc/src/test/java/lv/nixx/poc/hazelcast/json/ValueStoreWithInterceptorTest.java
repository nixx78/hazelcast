package lv.nixx.poc.hazelcast.json;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapInterceptor;
import lombok.SneakyThrows;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Account;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ValueStoreWithInterceptorTest {

    private final HazelcastInstance hazelcast = HazelcastTestInstance.get();

    @Test
    public void jsonValueStoreSample() {

        IMap<String, Account> map = hazelcast.getMap("map.account");
        map.clear();
        map.addInterceptor(new ConvertOnPutMapInterceptor());

        map.put("id1", new Account("id1", "name1"));

        Account acc1 = map.get("id1");
        assertEquals("id1", acc1.getAccountId());
        assertEquals("name1", acc1.getName());
    }

    static class ConvertOnPutMapInterceptor implements MapInterceptor {

        @SneakyThrows
        @Override
        public Object interceptGet(Object o) {
            return o;
        }

        @SneakyThrows
        @Override
        public Object interceptPut(Object o, Object o1) {
            return o;
        }

        @Override
        public void afterGet(Object o) {
        }

        @Override
        public void afterPut(Object o) {
        }

        @Override
        public Object interceptRemove(Object o) {
            return null;
        }

        @Override
        public void afterRemove(Object o) {
        }
    }


}
