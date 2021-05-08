package railway.reservation.system.ExceptionHandling;
public class NoTrainExistException extends Exception {
    public NoTrainExistException(String s)
    {
        super(s);
    }
}
