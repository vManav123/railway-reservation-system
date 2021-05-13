package railway.reservation.system.ExceptionHandling;

public class TrainClassNotExistException extends Exception {
    public TrainClassNotExistException(String s) {
        super(s);
    }
}
