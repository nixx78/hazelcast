package lv.nixx.poc.hazelcast.listener;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@ToString
@AllArgsConstructor
class Message implements Serializable {
	Long id;
	String message;
}