package lv.nixx.poc.hazelcast.sql;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;
import com.hazelcast.sql.SqlService;
import lv.nixx.poc.hazelcast.HazelcastTestInstance;
import lv.nixx.poc.hazelcast.model.Transaction;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

public class SQLQueriesSandbox {

    HazelcastInstance hazelcastInstance = HazelcastTestInstance.get();

    @Test
    public void getDataSample() {

        IMap<String, Transaction> m = hazelcastInstance.getMap("txn_sql");

        m.putAll(
                Map.of(
                        "t1", new Transaction()
                                .setAccountId("account1")
                                .setTxnId("txnId.1")
                                .setAmount(BigDecimal.valueOf(400.00))
                                .setCurrency("EUR"),
                        "t2", new Transaction()
                                .setAccountId("account2")
                                .setTxnId("txnId.2")
                                .setAmount(BigDecimal.valueOf(100.00))
                                .setCurrency("EUR"),
                        "t3", new Transaction()
                                .setAccountId("account3")
                                .setTxnId("txnId.3")
                                .setAmount(BigDecimal.valueOf(300.00))
                                .setCurrency("EUR"),
                        "t4", new Transaction()
                                .setAccountId("account1")
                                .setTxnId("txnId.4")
                                .setAmount(BigDecimal.valueOf(3.00))
                                .setCurrency("EUR")


                )
        );


        SqlService sql = hazelcastInstance.getSql();

        SqlResult sqlResult = sql.execute("select accountId, txnId, currency from txn_sql");

        for (SqlRow sr : sqlResult) {
            System.out.println(sr.getObject(0) + ":" + sr.getObject(1));
        }

        System.out.println("================");

        SqlResult sqlResult1 = sql.execute("select accountId, txnId from txn_sql where accountId = ?", "account1");
        for (SqlRow sr : sqlResult1) {
            System.out.println(sr.getObject(0) + ":" + sr.getObject(1));
        }


    }
}
