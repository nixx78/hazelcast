package lv.nixx.poc.hazelcast;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectEvent;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class DistributionObjectSandbox {

    private HazelcastInstance hz;

    public DistributionObjectSandbox() {
        hz = HazelcastTestInstance.get();
        hz.addDistributedObjectListener(new DistObjListenerSample());
    }

    @Test
    public void distributedObjectSample() throws InterruptedException {

        final IMap<Object, Object> map1 = hz.getMap("Map1");
        map1.destroy();

        TimeUnit.SECONDS.sleep(1);

        // No error there.. This is dangerous
        System.out.println("Map size after destroy:" + map1.size());
    }

    static class DistObjListenerSample implements DistributedObjectListener {

        @Override
        public void distributedObjectCreated(DistributedObjectEvent event) {
            DistributedObject instance = event.getDistributedObject();
            System.out.println("Created " + instance.getName() + ", service=" + instance.getServiceName());
        }

        @Override
        public void distributedObjectDestroyed(DistributedObjectEvent event) {
            System.out.println("Destroyed " + event.getObjectName() + ", service=" + event.getServiceName());
        }
    }

}
