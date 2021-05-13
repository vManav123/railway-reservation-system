package railway.application.system.ExceptionHandling;

public class InsufficientBalanceInBankAccount extends Exception {
    public InsufficientBalanceInBankAccount(String s) {
        super(s);
    }
}
