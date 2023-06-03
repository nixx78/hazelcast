package lv.nixx.poc.hazelcast.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable, Comparable<Category>{
	private long blockId;
	private boolean isSelected;
	private String color;
	
	@Override
	public int compareTo(Category o) {
		return 0;
	}
}
