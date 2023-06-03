package lv.nixx.poc.hazelcast.model;

import com.hazelcast.partition.PartitionAware;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public final  class TransactionKey implements PartitionAware<String>, Serializable {

    private String accountId;
    private String transactionId;

    @Override
    public String getPartitionKey() {
        return accountId;
    }
}
