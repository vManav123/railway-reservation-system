package railway.application.system.exceptionHandling;

public class TransactionUnSuccessfulException extends Exception {
    public TransactionUnSuccessfulException(String s) {
        super(s);
    }
}
