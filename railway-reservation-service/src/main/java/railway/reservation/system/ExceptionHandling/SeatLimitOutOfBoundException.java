package railway.reservation.system.ExceptionHandling;

public class SeatLimitOutOfBoundException extends Exception {
    public SeatLimitOutOfBoundException(String s) {
        super(s);
    }
}
