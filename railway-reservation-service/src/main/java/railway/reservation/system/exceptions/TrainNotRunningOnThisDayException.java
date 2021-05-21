package railway.reservation.system.exceptions;

public class TrainNotRunningOnThisDayException extends Exception {
    public TrainNotRunningOnThisDayException(String s) {
        super(s);
    }
}
