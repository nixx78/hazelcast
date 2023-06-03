package lv.nixx.poc.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.IMap;
import lv.nixx.poc.hazelcast.model.State;
import lv.nixx.poc.hazelcast.model.Transaction;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TransactionDataService {

    private IMap<String, Map<String, Transaction>> remoteMap;

    //
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        remoteMap = hazelcastInstance.getMap("txn.test");
    }

    public Transaction add(Transaction txn) {
        String accountId = txn.getAccountId();
        return remoteMap.executeOnKey(accountId, new TransactionAdd(txn));
    }

    public Transaction update(Transaction txn) {
        String accountId = txn.getAccountId();
        return remoteMap.executeOnKey(accountId, new TransactionUpdater(txn));
    }

    public Transaction deleteTransaction(String accountId, String txnId) {
        return remoteMap.executeOnKey(accountId, new TransactionRemover(txnId));
    }

    public Transaction changeState(String accountId, String txnId, State state) {

        return remoteMap.executeOnKey(accountId, (EntryProcessor<String, Map<String, Transaction>, Transaction>) entry -> {

            if (entry == null) {
                return null;
            }

            Map<String, Transaction> value = entry.getValue();
            if (value == null) {
                return null;
            }

            Transaction updatedTransaction = value.computeIfPresent(txnId, (k, v) -> {
                v.setState(state);
                return v;
            });

            entry.setValue(value);

            return updatedTransaction;
        });

    }

    public void recalculateInterests() {

        Map<String, Map<String, Transaction>> globalMap = new HashMap<>();

        remoteMap.executeOnEntries((EntryProcessor<String, Map<String, Transaction>, Transaction>) entry -> {

            Map<String, Transaction> map = entry.getValue();
            if (map == null) {
                return null;
            }

            map.values().forEach(t -> {
                BigDecimal amount = t.getAmount();
                t.setInterest(amount.multiply(BigDecimal.valueOf(0.1)));
            });

            entry.setValue(map);

            Map<String, Map<String, Transaction>> res = new HashMap<>();
            res.put(entry.getKey(), map);

            globalMap.putAll(res);

            return null;
        });

    }

    public Transaction getTransaction(String accountId, String txnId) {
        return Optional.ofNullable(remoteMap.get(accountId))
                .map(t -> t.get(txnId))
                .orElse(null);
    }

    public Map<String, Map<String, Transaction>> getAllData() {
        return remoteMap;
    }

    public void clearAll() {
        remoteMap.clear();
    }


    static class TransactionAdd implements EntryProcessor<String, Map<String, Transaction>, Transaction> {

        private final Transaction txn;

        TransactionAdd(Transaction txn) {
            this.txn = txn;
        }

        @Override
        public Transaction process(Map.Entry<String, Map<String, Transaction>> entry) {

            Map<String, Transaction> map = Optional.ofNullable(entry.getValue()).orElse(new HashMap<>());
            final String txnId = txn.getTxnId();
            map.put(txnId, txn);

            entry.setValue(map);
            return txn;
        }
    }


    static class TransactionUpdater implements EntryProcessor<String, Map<String, Transaction>, Transaction> {

        private final Transaction txn;

        TransactionUpdater(Transaction txn) {
            this.txn = txn;
        }

        @Override
        public Transaction process(Map.Entry<String, Map<String, Transaction>> entry) {

            Map<String, Transaction> map = entry.getValue();
            if (map == null) {
                return null;
            }

            Transaction transaction = map.computeIfPresent(txn.getTxnId(), (k, v) -> txn);
            entry.setValue(map);

            return transaction;
        }
    }

    static class TransactionRemover implements EntryProcessor<String, Map<String, Transaction>, Transaction> {

        private final String txnId;

        TransactionRemover(String txnId) {
            this.txnId = txnId;
        }

        @Override
        public Transaction process(Map.Entry<String, Map<String, Transaction>> entry) {
            Map<String, Transaction> value = entry.getValue();
            if (value == null) {
                return null;
            }

            entry.setValue(value);
            return value.remove(txnId);
        }
    }

}
