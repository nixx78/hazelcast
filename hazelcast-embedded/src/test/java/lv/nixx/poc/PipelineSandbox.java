package lv.nixx.poc;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.core.metrics.JobMetrics;
import com.hazelcast.jet.core.metrics.Metrics;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.hazelcast.function.Functions.wholeItem;
import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;
import static org.junit.Assert.assertEquals;

public class PipelineSandbox {

    private final String LIST_NAME = "wordList";
    private final String MAP_NAME = "wordMap";

    private Pipeline createPipeLine() {
        Pipeline p = Pipeline.create();
        p.readFrom(Sources.<String>list(LIST_NAME))
                .flatMap(word -> traverseArray(word.toLowerCase().split("\\W+")))
                .filter(word -> {
                            boolean empty = word.isEmpty();
                            if (empty) {
                                Metrics.metric("empty").increment();
                            }
                            Metrics.metric("total").increment();

                            return !word.isEmpty();
                        }
                )
                .groupingKey(wholeItem())
                .aggregate(counting())
                .writeTo(Sinks.map(MAP_NAME));
        return p;
    }

    public Long countWord(List<String> sentences, String word) {
        long count = 0;
        JetInstance jet = Jet.newJetInstance();
        try {
            List<String> textList = jet.getList(LIST_NAME);
            textList.addAll(sentences);
            Pipeline p = createPipeLine();
            Job job = jet.newJob(p, new JobConfig()
                    .setName("word-count-job")
                    .setMetricsEnabled(true)
                    .setStoreMetricsAfterJobCompletion(true)
            );
            job.join();

            Map<String, Long> counts = jet.getMap(MAP_NAME);
            count = counts.get(word);

            JobMetrics metrics = job.getMetrics();
        } finally {
            Jet.shutdownAll();
        }
        return count;
    }

    @Test
    public void test() {
        long c = countWord(List.of("A", "A", "B", "C", "D", "A", "", ""), "a");
        assertEquals(3L, c);
    }


}
