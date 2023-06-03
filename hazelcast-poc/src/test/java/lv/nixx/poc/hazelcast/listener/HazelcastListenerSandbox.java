package lv.nixx.poc.hazelcast.listener;

import com.hazelcast.config.Config;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import com.hazelcast.map.MapInterceptor;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapClearedListener;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class HazelcastListenerSandbox {

	private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

	@Test
	public void testConfigListenerRegistration() throws InterruptedException {
		Config config = hazelcastInstance.getConfig();

		MapConfig mapWithListenerConfig =  config.getMapConfig("mapWithListener");

		EntryListenerConfig entryListenerConfig = new EntryListenerConfig();
		entryListenerConfig.setImplementation(new MyEntryListener());

		// In this case, we duplicate listeners
		mapWithListenerConfig.addEntryListenerConfig(entryListenerConfig);
		mapWithListenerConfig.addEntryListenerConfig(entryListenerConfig);

		List<EntryListenerConfig> c1 = mapWithListenerConfig.getEntryListenerConfigs();
		assertEquals(2, c1.size());

		IMap<String, String> map = hazelcastInstance.getMap("mapWithListener");
		map.put("key", "value");

		List<EntryListenerConfig> c2 = config.getMapConfig("mapWithoutListener").getEntryListenerConfigs();
		assertEquals(0, c2.size());
	}
	
	@Test
	public void entryListenerTest() {
		// https://docs.hazelcast.org/docs/latest-development/manual/html/Distributed_Events/Event_Listener_for_Members/Listening_for_Map_Events.html
		
		IMap<String, String> map = hazelcastInstance.getMap("abc");
		UUID s = map.addEntryListener(new MyEntryListener(), true);

		map.addInterceptor(new MapInterceptorImpl());

		System.out.println("Listener1 Id:" + s);


		map.put("One", "One.value");

		map.replace("One", "One.Replaced");
		map.remove("One");
		
		map.put("Two", "Initial.Value");
		map.merge("Two", "UpdatedValue", (t1, t2) -> t1 + ":" + t2);
		
		map.computeIfAbsent("Three", t1 -> "Three.Value");
		
		map.delete("Two"); // oldValue will be null
		
		map.put("Four", "Three.Initial.Value");
		map.put("Four", "Three.NewValue.Value");
		
	}

	@Test
	public void clearAllListenerTest() {

		IMap<String, String> map = hazelcastInstance.getMap("map.forClearAll");
		UUID s = map.addEntryListener(new ClearAllListener(), true);

		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		map.put("k4", "v4");

		System.out.println("Size: " + map.size());
		map.clear();
	}

	static class MyEntryListener implements EntryAddedListener<String, String>,
									 EntryUpdatedListener<String, String>,
									 EntryRemovedListener<String, String>{
		
		@Override
		public void entryAdded(EntryEvent<String, String> entryEvent) {
			System.out.println("Tread:" + Thread.currentThread() + ":" + entryEvent);
		}

		@Override
		public void entryUpdated(EntryEvent<String, String> event) {
			System.out.println(event);
		}

		@Override
		public void entryRemoved(EntryEvent<String, String> event) {
			System.out.println(event);
		}
	}
	
	static class MapInterceptorImpl implements MapInterceptor {

		@Override
		public Object interceptGet(Object value) {
			System.out.println("interceptGet: " + value);
			return value;
		}

		@Override
		public void afterGet(Object value) {
			System.out.println("afterGet: " + value);
		}

		@Override
		public Object interceptPut(Object oldValue, Object newValue) {
			System.out.println("interceptPut, oldValue: " + oldValue + " newValue: " + newValue);
			return newValue;
		}

		@Override
		public void afterPut(Object value) {
			System.out.println("afterPut: " + value);
		}

		@Override
		public Object interceptRemove(Object removedValue) {
			System.out.println("interceptRemove: " + removedValue);
			return removedValue;
		}

		@Override
		public void afterRemove(Object oldValue) {
			System.out.println("afterRemove: " + oldValue);
		}
		
	}

	static class ClearAllListener implements MapClearedListener {
		@Override
		public void mapCleared(MapEvent event) {
			System.out.println("Map clear event: " + event);
		}

	}


}
