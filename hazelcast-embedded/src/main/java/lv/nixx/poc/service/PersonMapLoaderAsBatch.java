package lv.nixx.poc.service;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.map.IMap;
import lv.nixx.poc.model.ExternalResource;
import lv.nixx.poc.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PersonMapLoaderAsBatch {

    private static final Logger LOG = LoggerFactory.getLogger(PersonMapLoaderAsBatch.class);

    private final JetInstance jetInstance;

    @Autowired
    public PersonMapLoaderAsBatch(JetInstance jetInstance) {
        this.jetInstance = jetInstance;
    }

    public void load() {
        Map<String, Person> data = ExternalResource.data;

        long stTime = System.currentTimeMillis();

        IMap<String, Person> map = jetInstance.getMap("person.map.batch");
        map.putAll(data);

        LOG.info("Batch load time [{}]", (System.currentTimeMillis() - stTime));
    }


}
