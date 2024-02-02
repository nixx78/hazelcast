package lv.nixx.poc.hazelcast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.cache.Cache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//@RestController
//@RequestMapping("/helloPreConfiguredCache")
public class PreConfiguredCacheController {

    Cache<String, String> xmlCache;

    @Autowired
    public PreConfiguredCacheController(Cache<String, String> xmlCache) {
        this.xmlCache = xmlCache;
    }

    @PostMapping("/value")
    public void addValue(@RequestParam String key, @RequestParam String value) {
        xmlCache.put(key, value);
    }

    @GetMapping("/values")
    public Map<String, String> getAll() {
        Iterator<Cache.Entry<String, String>> iterator = xmlCache.iterator();

        Map<String, String> allValues = new HashMap<>();
        while (iterator.hasNext()) {
            Cache.Entry<String, String> entry = iterator.next();
            allValues.put(entry.getKey(), entry.getValue());
        }

        return allValues;
    }

}
