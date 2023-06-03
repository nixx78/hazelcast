package lv.nixx.poc.hazelcast.listener;

import com.hazelcast.core.EntryAdapter;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import com.hazelcast.map.listener.MapListener;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Account;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MapListenerSandboxTest {

    private final static Logger LOG = LoggerFactory.getLogger(MapListenerSandboxTest.class);

    private final HazelcastInstance hazelcast = HazelcastTestInstance.get();

    @Test
    void listenMapEvents() {
        IMap<String, Account> map = hazelcast.getMap("accout.map.for.listener");
        map.addEntryListener(new Listener(), true);

        map.put("acc1", new Account("acc1", "First account"));
        map.put("acc1", new Account("acc1", "First account"));

        map.merge("acc1", new Account("acc1", "update"), (t1, t2) -> new Account(t1.getAccountId(), t1.getName() + "." + t2.getName()));

        Account acc1 = map.get("acc1");
        LOG.info("Merged account [{}]", acc1);

        map.put("acc2", new Account("acc2", "Second account"));
        map.remove("acc2");

        map.clear();
    }

    static class Listener extends EntryAdapter<String, Account> implements MapListener {

        @Override
        public void mapCleared(MapEvent event) {
            super.mapCleared(event);
        }

        @Override
        public void onEntryEvent(EntryEvent<String, Account> event) {
            LOG.info("OnEntry event [{}]", event);
        }

        @Override
        public void onMapEvent(MapEvent event) {
            LOG.info("onMapEvent event [{}]", event);
        }
    }


}
