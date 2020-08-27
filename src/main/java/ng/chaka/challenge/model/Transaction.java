package ng.chaka.challenge.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
