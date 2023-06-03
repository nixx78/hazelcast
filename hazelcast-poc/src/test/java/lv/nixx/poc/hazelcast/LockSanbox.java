package lv.nixx.poc.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.Test;

public class LockSanbox {

	private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

	@Test
	public void mapLockNotExistingKeySample() {
		
		final Integer key = 10; // Key can exist or not, does't matter
		IMap<Integer, String> map = hazelcastInstance.getMap("test.map");

		map.lock(key);
		System.out.println("Is key [" + key + "] is locked [" + map.isLocked(key) + "]");
		
		map.put(key, "Value.During.Lock");
		System.out.println(map.get(key));
		
		map.unlock(key);
		
		System.out.println("Unlocked");
		System.out.println("Is key [" + key + "] is locked [" + map.isLocked(key) + "]");
	}
	
	@Test
	public void distributedLockSample() throws InterruptedException {

		//FIXME Migrate to Hazelcast 4.2
		
//		ILock dLock = hazelcastInstance.getLock("dLock.key");
//		System.out.println("Lock [" + dLock + " locked: " + dLock.isLocked() );
//
//		dLock.lock();
//		dLock.lock();
//		dLock.lock();
//
//		System.out.println("Lock [" + dLock + " count: " + dLock.getLockCount() );
//
//		dLock.unlock();
//
//		System.out.println("Lock [" + dLock + " count after one unlock: " + dLock.getLockCount() );
//
//		dLock.forceUnlock();
//		System.out.println("Lock [" + dLock + " count after forseUnlock: " + dLock.getLockCount() );
//
//		dLock.lock(500, TimeUnit.MILLISECONDS);
//
//		TimeUnit.MILLISECONDS.sleep(300);
//		System.out.println("Lock [" + dLock + " remaining lease time: " + dLock.getRemainingLeaseTime() );
//
//		dLock.unlock();
//		System.out.println("Lock [" + dLock + " remaining lease time after unlock: " + dLock.getRemainingLeaseTime() );
	}


}
