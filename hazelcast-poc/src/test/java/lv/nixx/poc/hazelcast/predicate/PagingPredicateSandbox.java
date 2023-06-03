package lv.nixx.poc.hazelcast.predicate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicates;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import org.junit.Before;
import org.junit.Test;

public class PagingPredicateSandbox {

    private final HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    private final IMap<Integer, String> data = hazelcastInstance.getMap("pages");

    @Before
    public void init() {
        data.clear();
        createTestData();
    }

    @Test
    public void pagingSample() {

        PagingPredicate<Integer, String> pagingPredicate = Predicates.pagingPredicate(
                Predicates.notEqual("this", "NOT_VALID"),
//                Map.Entry.comparingByKey(),
                10);

        System.out.println("Page: " + pagingPredicate.getPage() + " : " + data.values(pagingPredicate));

        // Set up next page
        pagingPredicate.nextPage();

        // Retrieve next page
        System.out.println("Page: " + pagingPredicate.getPage() + " : " + data.values(pagingPredicate));

        // Retrieve by page number
        pagingPredicate.setPage(2);
        System.out.println("Page: " + pagingPredicate.getPage() + " : " + data.values(pagingPredicate));
    }

    private void createTestData() {
        for (int i = 1; i <= 25; i++) {
            data.put(i, "data." + i);
        }
        data.put(777, "NOT_VALID");
    }

}
