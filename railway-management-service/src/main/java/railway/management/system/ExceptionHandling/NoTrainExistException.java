package railway.management.system.ExceptionHandling;

public class NoTrainExistException extends Exception {
    public NoTrainExistException(String s)
    {
        super(s);
    }
}
