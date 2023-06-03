package lv.nixx.poc.hazelcast.pipeline;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Pipelining;
import com.hazelcast.map.IMap;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PipeliningSample {

    private final IMap<Integer, String> map;

    public PipeliningSample() {
        HazelcastInstance hazelcast = HazelcastTestInstance.get();
        this.map = hazelcast.getMap("simpleValues");
    }

    @Before
    public void init() {
        this.map.clear();
    }

    @Test
    public void processingWithPipeline() throws Exception {

        long s = System.currentTimeMillis();
        Pipelining<String> pipelining = new Pipelining<>(10);
        for (int k = 0; k < 100_000; k++) {
            pipelining.add(map.getAsync(k));
        }

        assertEquals(100_000, pipelining.results().size());
        System.out.println("Processing with Pipeline time:" + (System.currentTimeMillis() - s));
    }

    @Test
    public void processingWithoutPipeline() {

        long s = System.currentTimeMillis();
        List<String> results = new ArrayList<>();
        for (int k = 0; k < 100_000; k++) {
            results.add(map.get(k));
        }

        assertEquals(100_000, results.size());
        System.out.println("Processing without Pipeline time:" + (System.currentTimeMillis() - s));
    }

    private void fillMap() {
        for (int i = 1; i < 100_000; i++) {
            map.put(i, "Value" + i);
        }
    }
}
