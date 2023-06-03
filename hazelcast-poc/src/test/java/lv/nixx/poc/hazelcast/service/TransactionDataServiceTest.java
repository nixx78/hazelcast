package lv.nixx.poc.hazelcast.service;

import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.BigDecimal.ZERO;
import static lv.nixx.poc.hazelcast.model.State.COMPLETED;
import static lv.nixx.poc.hazelcast.model.State.DRAFT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TransactionDataServiceTest {

    private static final String NOT_EXISTING_ACCOUNT_ID = "NotExistingAccountId";
    private static final String NOT_EXISTING_TXN_ID = "NotExistingId";

    private static final String USD = "USD";
    private static final String EUR = "EUR";
    private static final String RUB = "RUB";

    private String accountId = "accountId.1";
    private String txnId2 = "id2";
    private String txnId1 = "id1";

    private TransactionDataService transactionDataService;


    @Before
    public void init() {
        transactionDataService = new TransactionDataService();
        transactionDataService.setHazelcastInstance(HazelcastTestInstance.get());
    }

    @Test
    public void addTest() {

        transactionDataService.add(new Transaction(accountId, txnId1, USD, BigDecimal.valueOf(10.01), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, txnId2, EUR, BigDecimal.valueOf(10.01), DRAFT, ZERO));

        Map<String, Map<String, Transaction>> allData = transactionDataService.getAllData();
        System.out.println(allData.get(accountId));

        assertEquals(2, allData.get(accountId) .size());
    }

    @Test
    public void updateTest() {
        transactionDataService.add(new Transaction(accountId, txnId1, USD, BigDecimal.valueOf(10.01), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, txnId2, EUR, BigDecimal.valueOf(10.01), DRAFT, ZERO));

        Transaction updatedTxn = transactionDataService.update(new Transaction(accountId, txnId2, RUB, BigDecimal.valueOf(10.01), DRAFT, ZERO));
        assertEquals("RUB", updatedTxn.getCurrency());

        assertEquals("RUB", transactionDataService.getTransaction(accountId, txnId2).getCurrency());

        assertNull(transactionDataService.update(new Transaction(accountId, NOT_EXISTING_TXN_ID, RUB, BigDecimal.valueOf(10.01), DRAFT, ZERO)));
        assertNull(transactionDataService.getTransaction(accountId, NOT_EXISTING_TXN_ID));

        assertNull(transactionDataService.update(new Transaction(NOT_EXISTING_ACCOUNT_ID, txnId2, RUB, BigDecimal.valueOf(10.01), DRAFT, ZERO)));
        assertNull(transactionDataService.getTransaction(NOT_EXISTING_ACCOUNT_ID, txnId2));
    }

    @Test
    public void deleteTest() {
        transactionDataService.add(new Transaction(accountId, txnId1, USD, BigDecimal.valueOf(10.01), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, txnId2, USD, BigDecimal.valueOf(10.01), DRAFT, ZERO));

        Transaction deletedTransaction = transactionDataService.deleteTransaction(accountId, txnId2);
        assertEquals(txnId2, deletedTransaction.getTxnId());

        assertNull(transactionDataService.getTransaction(accountId, txnId2));
    }

    @Test
    public void changeStateTest() {
        transactionDataService.add(new Transaction(accountId, txnId1, USD, BigDecimal.valueOf(10.01), DRAFT, ZERO));

        Transaction changedTxn = transactionDataService.changeState(accountId, txnId1, COMPLETED);
        assertEquals(COMPLETED, changedTxn.getState());

        assertEquals(COMPLETED, transactionDataService.getTransaction(accountId, txnId1).getState());

        assertNull(transactionDataService.changeState(accountId, NOT_EXISTING_TXN_ID, COMPLETED));
        assertNull(transactionDataService.getTransaction(accountId, NOT_EXISTING_TXN_ID));

        assertNull(transactionDataService.changeState(NOT_EXISTING_ACCOUNT_ID, txnId2, COMPLETED));
        assertNull(transactionDataService.getTransaction(NOT_EXISTING_ACCOUNT_ID, txnId2));
    }

    @Test
    public void recalculateInterests() {

        transactionDataService.add(new Transaction(accountId, txnId1, USD, BigDecimal.valueOf(10.0), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, txnId2, USD, BigDecimal.valueOf(20.0), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, "id3", EUR, BigDecimal.valueOf(30.0), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, "id4", RUB, BigDecimal.valueOf(40.0), DRAFT, ZERO));
        transactionDataService.add(new Transaction(accountId, "id5", EUR, BigDecimal.valueOf(50.0), DRAFT, ZERO));

        transactionDataService.recalculateInterests();

        Map<String, Map<String, Transaction>> allData = transactionDataService.getAllData();

        allData.values().forEach(System.out::println);

        assertEquals(5, allData.get(accountId).size());
    }

}
