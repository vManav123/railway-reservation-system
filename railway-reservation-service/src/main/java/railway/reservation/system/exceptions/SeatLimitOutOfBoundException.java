package railway.reservation.system.exceptions;

public class SeatLimitOutOfBoundException extends Exception {
    public SeatLimitOutOfBoundException(String s) {
        super(s);
    }
}
