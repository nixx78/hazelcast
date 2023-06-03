package lv.nixx.poc.hazelcast.cache;

import com.hazelcast.query.Predicate;

import java.util.Collection;
import java.util.Collections;

public interface CrudCacheOperations<K, V> {
    V get(K key);

    void add(K key, V person);

    V update(K key, V person);

    V remove(K key);

    Collection<V> getValues(Predicate<K, V> p);

    int size();

    void clearAll();

    default Collection<V> getAll() {
        //FIXME Implement method
        return Collections.emptyList();
    }

}
