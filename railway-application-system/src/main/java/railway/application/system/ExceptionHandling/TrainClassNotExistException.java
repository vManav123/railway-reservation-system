package railway.application.system.ExceptionHandling;

public class TrainClassNotExistException extends Exception {
    public TrainClassNotExistException(String s)
    {
        super(s);
    }
}
