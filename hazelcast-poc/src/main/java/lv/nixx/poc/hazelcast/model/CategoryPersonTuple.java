package lv.nixx.poc.hazelcast.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CategoryPersonTuple {

	@NonNull
	private Category category;
	
	@NonNull
	private Person person;
	
}
