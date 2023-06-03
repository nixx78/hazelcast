package lv.nixx.poc.hazelcast.listener;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.LocalTopicStats;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class TopicListenerSandbox {

    private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    @Test
    public void sendAndReceiveMessageFromTopic() throws InterruptedException {

        ITopic<Object> messageTopic = hazelcastInstance.getTopic("MessageTopic");

        UUID s = messageTopic.addMessageListener(m-> {
            System.out.println(new Date(m.getPublishTime()) + " : payload:" + m.getMessageObject());
        });

        System.out.println("Message listener is added with ID:" + s);

        messageTopic.publish("Message1");
        messageTopic.publish("Message2");

        SECONDS.sleep(1);

        LocalTopicStats localTopicStats = messageTopic.getLocalTopicStats();
        System.out.println(localTopicStats);

        assertEquals(2, localTopicStats.getReceiveOperationCount());
    }

}
