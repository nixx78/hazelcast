package lv.nixx.poc.hazelcast.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class Account implements Serializable {

    private final String accountId;
    private final String name;

    @JsonCreator
    public Account(@JsonProperty("accountId") String accountId, @JsonProperty("name") String name) {
        this.accountId = accountId;
        this.name = name;
    }
}
