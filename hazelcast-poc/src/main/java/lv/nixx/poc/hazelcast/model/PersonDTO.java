package lv.nixx.poc.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@Getter
@ToString
public class PersonDTO {
    private int key;
    private String name;
    private Date dateOfBirth;
}