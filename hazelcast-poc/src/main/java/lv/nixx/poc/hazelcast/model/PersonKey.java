package lv.nixx.poc.hazelcast.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuppressWarnings("serial")
@AllArgsConstructor
@NoArgsConstructor
public class PersonKey implements Serializable {
	private long selectionId;
	private Category category;	
}
