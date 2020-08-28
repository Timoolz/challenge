package ng.chaka.challenge.service;

import ng.chaka.challenge.model.Statistic;
import ng.chaka.challenge.model.Transaction;
import ng.chaka.challenge.model.TransactionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BaseService {


    private static final String UTC_ZONE_STRING = "UTC";
    private static final int SIXTY_SECONDS_INTEGER = 60;

    private CopyOnWriteArrayList<Transaction> transactionList = new CopyOnWriteArrayList<>();


    public ResponseEntity createTransaction(TransactionRequest transactionRequest) {
        if (!validTransactionRequest(transactionRequest)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction();
        try {
            transaction.setTimestamp(LocalDateTime.parse(transactionRequest.getTimestamp(), DateTimeFormatter.ISO_ZONED_DATE_TIME));
            transaction.setAmount(new BigDecimal(transactionRequest.getAmount()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        //IF TransactionDate is in the future
        if (LocalDateTime.now().isBefore(transaction.getTimestamp())) {
            return new ResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        transactionList.add(transaction);

        //IF TransactionDate is older than 60 seconds
        if (Duration.between(transaction.getTimestamp(), LocalDateTime.now().atZone(ZoneId.of(UTC_ZONE_STRING)))
                .compareTo(Duration.ofSeconds(SIXTY_SECONDS_INTEGER)) > 0) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }


        return new ResponseEntity(HttpStatus.CREATED);
    }


    private boolean validTransactionRequest(TransactionRequest request) {
        if(request == null){
            return false;
        }

        if (!StringUtils.hasText(request.getTimestamp())) {
            return false;
        }

        if (!StringUtils.hasText(request.getAmount())) {
            return false;
        }

        return true;
    }

    public ResponseEntity<Statistic> getStatistics() {
        LocalDateTime currentTime = LocalDateTime.now();
        Statistic statistic = new Statistic();

        //GET SUM IN THE LAST 60 SECONDS
        Optional<BigDecimal> sum = transactionList.stream()
                .filter(t -> Duration.between(t.getTimestamp(), currentTime.atZone(ZoneId.of(UTC_ZONE_STRING))).compareTo(Duration.ofSeconds(SIXTY_SECONDS_INTEGER)) < 0)
                .map(Transaction::getAmount)
                .reduce(BigDecimal::add);
        sum.ifPresent(statistic::setSum);

        //GET COUNT IN THE LAST 60 SECONDS
        long count = transactionList.stream()
                .filter(t -> Duration.between(t.getTimestamp(), currentTime.atZone(ZoneId.of(UTC_ZONE_STRING))).compareTo(Duration.ofSeconds(SIXTY_SECONDS_INTEGER)) < 0)
                .count();
        statistic.setCount(count);

        //GET THE AVERAGE
        if (sum.isPresent()) {
            BigDecimal average = sum.get().divide(BigDecimal.valueOf(count));
            statistic.setAvg(average);
        }


        //GET MIN IN THE LAST 60 SECONDS
        Optional<BigDecimal> min = transactionList.stream()
                .filter(t -> Duration.between(t.getTimestamp(), currentTime.atZone(ZoneId.of(UTC_ZONE_STRING))).compareTo(Duration.ofSeconds(SIXTY_SECONDS_INTEGER)) < 0)
                .map(Transaction::getAmount)
                .min(BigDecimal::compareTo);
        min.ifPresent(statistic::setMin);

        //GET MAX IN THE LAST 60 SECONDS
        Optional<BigDecimal> max = transactionList.stream()
                .filter(t -> Duration.between(t.getTimestamp(), currentTime.atZone(ZoneId.of(UTC_ZONE_STRING))).compareTo(Duration.ofSeconds(SIXTY_SECONDS_INTEGER)) < 0)
                .map(Transaction::getAmount)
                .max(BigDecimal::compareTo);
        max.ifPresent(statistic::setMax);


        roundStatistic(statistic);
        return new ResponseEntity(statistic, HttpStatus.OK);
    }

    private void roundStatistic(Statistic statistic) {
        if (statistic.getMin() != null)
            statistic.setMin(statistic.getMin().setScale(2, BigDecimal.ROUND_HALF_UP));
        if (statistic.getMax() != null)
            statistic.setMax(statistic.getMax().setScale(2, BigDecimal.ROUND_HALF_UP));
        if (statistic.getSum() != null)
            statistic.setSum(statistic.getSum().setScale(2, BigDecimal.ROUND_HALF_UP));
        if (statistic.getAvg() != null)
            statistic.setAvg(statistic.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP));

    }

    public ResponseEntity deleteAllTransactions() {
        transactionList.clear();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }




    public int maxBinaryGap(int N) {
        int maxGap = 0;
        int currentGap = 0;
        boolean started = false;
        String binaryRepresentation = Integer.toBinaryString(N);
        for (Character character : binaryRepresentation.toCharArray()) {
            if (character.equals('0') && started) {
                currentGap++;
            }
            if (character.equals('1') && !started) {
                started = true;
            }
            if (character.equals('1')) {
                if(currentGap> maxGap){
                    maxGap = currentGap;
                }
                currentGap = 0;
            }

        }
        return maxGap;
    }

}
