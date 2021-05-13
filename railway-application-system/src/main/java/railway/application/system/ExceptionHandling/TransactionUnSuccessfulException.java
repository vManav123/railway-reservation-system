package railway.application.system.ExceptionHandling;

public class TransactionUnSuccessfulException extends Exception {
    public TransactionUnSuccessfulException(String s) {
        super(s);
    }
}
