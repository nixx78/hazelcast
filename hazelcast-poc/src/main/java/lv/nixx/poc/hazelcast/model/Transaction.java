package lv.nixx.poc.hazelcast.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Transaction implements Serializable {

    private String accountId;

    private String txnId;
    private String currency;
    private BigDecimal amount;
    private State state;
    private BigDecimal interest;

}
