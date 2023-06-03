package lv.nixx.poc.hazelcast.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of="id")
@Builder(toBuilder=true)
public class Person implements Serializable {
	
	@NonNull
	private Integer id;
	
	@NonNull
	private String name;
	
	@NonNull
	private Date age;

	private Collection<String> state;
	private Map<String, String> map = new HashMap<>();

	private Map<String, String> properties = new HashMap<>();

	public Person(@NonNull Integer id, @NonNull String name, Date age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Person put(String key, String value) {
		properties.put(key, value);
		return this;
	}

}
