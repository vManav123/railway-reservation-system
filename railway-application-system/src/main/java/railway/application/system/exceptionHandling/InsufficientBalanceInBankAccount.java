package railway.application.system.exceptionHandling;

public class InsufficientBalanceInBankAccount extends Exception {
    public InsufficientBalanceInBankAccount(String s) {
        super(s);
    }
}
