package lv.nixx.poc.hazelcast.cache;

import com.hazelcast.query.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Setter
@RequiredArgsConstructor
public class CrudCacheOperationsStub<K, V> implements CrudCacheOperations<K, V> {


    private Map<K,V> map = new HashMap<>();

    protected Map<K, V> getMap() {
        return map;
    }

    @Override
    public V get(K key) {
        return getMap().get(key);
    }

    @Override
    public void add(K key, V value) {
        getMap().put(key, value);
    }

    @Override
    public V update(K key, V value) {
        return getMap().replace(key, value);
    }

    @Override
    public V remove(K key) {
        return getMap().remove(key);
    }

    @Override
    public int size() {
        return getMap().size();
    }

    @Override
    public void clearAll() {
        getMap().clear();
    }

    @Override
    public Collection<V> getValues(Predicate<K, V> p) {
//        return getMap().entrySet(p)
//                .stream()
//                .map(Entry::getValue)
//                .collect(Collectors.toList());

        return Collections.emptyList();
    }


}
