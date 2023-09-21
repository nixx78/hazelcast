package lv.nixx.poc.hazelcast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.cache.Cache;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/helloJCache")
public class HelloWorldCacheController {

    Cache<String, String> helloWorldCache;

    @Autowired
    public HelloWorldCacheController(Cache<String, String> helloWorldCache) {
        this.helloWorldCache = helloWorldCache;
    }

    @PostMapping("/value")
    public void addValue(@RequestParam String key, @RequestParam String value) {
        helloWorldCache.put(key, value);
    }

    @GetMapping("/values")
    public Map<String, String> getAll() {
        Iterator<Cache.Entry<String, String>> iterator = helloWorldCache.iterator();

        Map<String, String> allValues = new HashMap<>();
        while (iterator.hasNext()) {
            Cache.Entry<String, String> entry = iterator.next();
            allValues.put(entry.getKey(), entry.getValue());
        }

        return allValues;
    }

}
