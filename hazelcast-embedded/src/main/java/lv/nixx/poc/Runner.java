package lv.nixx.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//TODO https://docs.hazelcast.com/imdg/latest/management/rest-endpoint-groups.html
// https://docs.hazelcast.com/management-center/4.2021.03/clustered-rest.html
// https://docs.hazelcast.com/imdg/latest/clients/rest.html
// https://www.baeldung.com/hazelcast-jet

@SpringBootApplication
public class Runner {

	public static void main(String[] args) {
		SpringApplication.run(Runner.class, args);
	}

}