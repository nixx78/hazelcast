package lv.nixx.poc.controller;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.map.IMap;
import lv.nixx.poc.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PersonController {

    private final JetInstance jetInstance;

    @Autowired
    public PersonController(JetInstance jetInstance) {
        this.jetInstance = jetInstance;
    }

    private IMap<String, Person> retrieveMap() {
        return jetInstance.getMap("person.map");
    }

    @PostMapping("/person")
    public Person add(@RequestBody Person person) {
        retrieveMap().put(person.getId(), person);
        return person;
    }

    @GetMapping("/person/{key}")
    public Person get(@PathVariable(value = "key") String key) {
        return retrieveMap().get(key);
    }
}