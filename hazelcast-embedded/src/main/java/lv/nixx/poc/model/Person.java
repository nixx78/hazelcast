package lv.nixx.poc.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Person implements Serializable {
    private String id;
    private String name;
    private String surname;
}
