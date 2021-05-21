package railway.application.system.exceptionHandling;

public class TrainClassNotExistException extends Exception {
    public TrainClassNotExistException(String s)
    {
        super(s);
    }
}
