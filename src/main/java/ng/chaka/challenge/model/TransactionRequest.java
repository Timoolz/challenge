package ng.chaka.challenge.model;

public class TransactionRequest {
    private String amount;
    private String timestamp;

    public String getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
