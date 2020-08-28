package ng.chaka.challenge;

import ng.chaka.challenge.model.Statistic;
import ng.chaka.challenge.model.TransactionRequest;
import ng.chaka.challenge.service.BaseService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseServiceTest {

    @Autowired
    BaseService baseService;

    @Test
    public void t1_postInvalidJsonTransaction() {
        ResponseEntity responseEntity = null;
        TransactionRequest transactionRequest = null;
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(400, responseEntity.getStatusCode().value());
        transactionRequest = new TransactionRequest();


        transactionRequest.setAmount("12.3343");
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(400, responseEntity.getStatusCode().value());


    }

    @Test
    public void t2_postUnparseableTransaction() {

        ResponseEntity responseEntity = null;
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("hfhfh");
        transactionRequest.setTimestamp("2018-07-17T09:59:51.312Z");
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(422, responseEntity.getStatusCode().value());

        transactionRequest.setAmount("12.33433");
        transactionRequest.setTimestamp("12.33433");
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(422, responseEntity.getStatusCode().value());
    }

    @Test
    public void t3_post60sLateTransaction() {
        ResponseEntity responseEntity = null;
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10.00");
        transactionRequest.setTimestamp(Instant.now().minusSeconds(65).toString());
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(204, responseEntity.getStatusCode().value());
    }


    @Test
    public void t4_postFutureTransaction() {

        ResponseEntity responseEntity = null;
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10.00");
        transactionRequest.setTimestamp(Instant.now().plusSeconds(3600).toString());
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(422, responseEntity.getStatusCode().value());

    }


    @Test
    public void t5_postValidTransaction() {
        ResponseEntity responseEntity = null;
        //Post One transaction
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10.00");
        transactionRequest.setTimestamp(Instant.now().minusSeconds(1).toString());
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(201, responseEntity.getStatusCode().value());

        //post another transaction
        transactionRequest.setAmount("20.00");
        transactionRequest.setTimestamp(Instant.now().minusSeconds(1).toString());
        responseEntity = baseService.createTransaction(transactionRequest);
        Assert.assertEquals(201, responseEntity.getStatusCode().value());
    }

    @Test
    public void t6_getTransactionStatistics() {


        ResponseEntity<Statistic> responseEntity = null;
        responseEntity = baseService.getStatistics();
        Assert.assertEquals(200, responseEntity.getStatusCode().value());
        Assert.assertTrue(responseEntity.hasBody());

        Statistic statistic = responseEntity.getBody();
        Assert.assertNotNull(statistic);

        Assert.assertEquals(2, statistic.getCount());

        Assert.assertEquals(0, statistic.getSum().compareTo(BigDecimal.valueOf(30)));
        Assert.assertEquals(0, statistic.getAvg().compareTo(BigDecimal.valueOf(15)));
        Assert.assertEquals(0, statistic.getMin().compareTo(BigDecimal.valueOf(10)));
        Assert.assertEquals(0, statistic.getMax().compareTo(BigDecimal.valueOf(20)));

    }

    @Test
    public void t7_deleteAllTransactions() {
        ResponseEntity responseEntity = null;
        responseEntity = baseService.deleteAllTransactions();
        Assert.assertEquals(204, responseEntity.getStatusCode().value());

    }

    @Test
    public void t8_getTransactionStatisticsAfterDelete() {


        ResponseEntity<Statistic> responseEntity = null;
        responseEntity = baseService.getStatistics();
        Assert.assertEquals(200, responseEntity.getStatusCode().value());
        Assert.assertTrue(responseEntity.hasBody());

        Statistic statistic = responseEntity.getBody();
        Assert.assertNotNull(statistic);

        Assert.assertEquals(0, statistic.getCount());

        Assert.assertNull( statistic.getSum());
        Assert.assertNull( statistic.getAvg());
        Assert.assertNull( statistic.getMax());
        Assert.assertNull( statistic.getMin());


    }

}
