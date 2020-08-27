package ng.chaka.challenge.api;

import ng.chaka.challenge.model.Statistic;
import ng.chaka.challenge.model.TransactionRequest;
import ng.chaka.challenge.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @Autowired
    BaseService baseService;

    @RequestMapping(value = "transactions", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postTransaction(@RequestBody TransactionRequest transactionRequest) {

        return baseService.createTransaction(transactionRequest);
    }

    @RequestMapping(value = "statistics", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistic> getStatistics() {

        return baseService.getStatistics();
    }


    @RequestMapping(value = "transactions", method = RequestMethod.DELETE)
    public ResponseEntity deleteAllTransactions() {
        return baseService.deleteAllTransactions();
    }


}
