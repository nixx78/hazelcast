package lv.nixx.poc.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ExternalResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExternalResource.class);


    public static final Map<String, Person> data;

    static {

        long stTime = System.currentTimeMillis();

        data = new HashMap<>();
        for (int i = 0; i < 500_000; i++) {
            data.put("p" + i, new Person()
                    .setId("p1")
                    .setName("Name" + i)
                    .setSurname("Surname" + i));
        }

        LOG.info("Initial data load time [{}] milliseconds", (System.currentTimeMillis() - stTime));
    }
}
