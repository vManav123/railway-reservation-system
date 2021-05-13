package railway.reservation.system.ExceptionHandling;

public class BankNotExistException extends Exception {
    public BankNotExistException(String s) {
        super(s);
    }
}
