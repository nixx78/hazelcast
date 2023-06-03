package lv.nixx.poc.controller;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.map.IMap;
import lv.nixx.poc.model.Person;
import lv.nixx.poc.service.PersonMapLoaderAsBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonBatchController {

    private final JetInstance jetInstance;
    private final PersonMapLoaderAsBatch batchLoader;

    @Autowired
    public PersonBatchController(JetInstance jetInstance, PersonMapLoaderAsBatch batchLoader ) {
        this.jetInstance = jetInstance;
        this.batchLoader = batchLoader;
    }

    private IMap<String, Person> retrieveMap() {
        return jetInstance.getMap("person.map.batch");
    }

    @GetMapping("/person/batchload")
    public void load() {
        batchLoader.load();
    }

    @GetMapping("/personFromBatch/{key}")
    public Person getFromBatch(@PathVariable(value = "key") String key) {
        return retrieveMap().get(key);
    }

}