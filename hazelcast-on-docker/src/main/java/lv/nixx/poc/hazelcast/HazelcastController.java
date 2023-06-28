package lv.nixx.poc.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class HazelcastController {

    private static final Logger log = LoggerFactory.getLogger(HazelcastController.class);

    private final IMap<String, String> map;

    @Autowired
    public HazelcastController(HazelcastInstance hazelcastInstance) {
        this.map = hazelcastInstance.getMap("hpoc.sandbox");
    }

    @PostMapping("/entry")
    void addEntry(@RequestParam String key, @RequestParam String value) {
        this.map.put(key, value);
    }

    @GetMapping("/entry")
    Set<Map.Entry<String, String>> getAllValues() {
        Set<Map.Entry<String, String>> entries = this.map.entrySet();
        log.info("Entry count in map [{}]", entries.size());
        return entries;
    }

    @GetMapping("/addTestData")
    void addTestData() {
        this.map.putAll(
                Map.of(
                        "k1", "v1",
                        "k2", "v2",
                        "k3", "v3"
                )
        );
    }


}
