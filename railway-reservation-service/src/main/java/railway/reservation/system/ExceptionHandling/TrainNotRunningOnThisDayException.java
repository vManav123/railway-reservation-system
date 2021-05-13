package railway.reservation.system.ExceptionHandling;

public class TrainNotRunningOnThisDayException extends Exception {
    public TrainNotRunningOnThisDayException(String s) {
        super(s);
    }
}
