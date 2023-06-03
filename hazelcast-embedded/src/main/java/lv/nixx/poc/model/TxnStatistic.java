package lv.nixx.poc.model;

import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.Sources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.hazelcast.function.Functions.wholeItem;
import static com.hazelcast.jet.Traversers.traverseArray;
import static com.hazelcast.jet.aggregate.AggregateOperations.counting;

@Service
public class TxnStatistic {

    private final String MAP_NAME = "wordMap";

    private final JetInstance jet;

    @Autowired
    public TxnStatistic(JetInstance jetInstance) {
        this.jet = jetInstance;
    }


    public Statistics calculate(String word) {
        Pipeline p = createPipeLine();
        jet.newJob(p).join();

        Map<String, Long> counts = jet.getMap(MAP_NAME);
        long count = counts.get(word);

        return new Statistics().setCount(count);
    }

    private Pipeline createPipeLine() {
        Pipeline p = Pipeline.create();
        p.readFrom(Sources.files("./data"))
                .flatMap(word -> traverseArray(word.toLowerCase().split("\\W+")))
                .filter(word -> !word.isEmpty())
                .groupingKey(wholeItem())
                .aggregate(counting())
                .writeTo(Sinks.map(MAP_NAME));

        return p;
    }


}
