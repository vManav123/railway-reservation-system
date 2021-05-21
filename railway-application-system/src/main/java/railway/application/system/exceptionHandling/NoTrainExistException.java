package railway.application.system.exceptionHandling;
public class NoTrainExistException extends Exception {
    public NoTrainExistException(String s)
    {
        super(s);
    }
}
