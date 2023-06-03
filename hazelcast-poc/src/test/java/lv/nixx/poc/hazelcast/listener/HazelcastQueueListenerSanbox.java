package lv.nixx.poc.hazelcast.listener;

import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.HazelcastInstance;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import org.junit.Test;

public class HazelcastQueueListenerSanbox {

	private static final String QUEUE_NAME = "simpleQueue";
	private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();
	
	@Test
	public void queueListenerTest() {
		IQueue<Message> producerQueue = hazelcastInstance.getQueue(QUEUE_NAME);

		producerQueue.addItemListener(new MyItemListener(), true);
	    
	    producerQueue.add(new Message(1L, "Message1"));
	    producerQueue.add(new Message(2L, "Message2"));
	    producerQueue.add(new Message(3L, "Message3"));

	}

	class MyItemListener implements ItemListener<Message> {
		IQueue<Message> consumerQueue = hazelcastInstance.getQueue(QUEUE_NAME);
		
		@Override
		public void itemAdded(ItemEvent<Message> item) {
			// ItemAdded not remove data from queue, we should call consumerQueue.remove...
			System.out.println("Tread:" + Thread.currentThread() + ":" + item + ":" + consumerQueue.size() + ":" + consumerQueue.remove(item.getItem()));
		}

		@Override
		public void itemRemoved(ItemEvent<Message> item) {
			System.out.println("Tread:" + Thread.currentThread() + ":" + item);
		}
	}
	

}
