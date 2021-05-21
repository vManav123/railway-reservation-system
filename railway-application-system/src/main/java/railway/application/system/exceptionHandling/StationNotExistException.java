package railway.application.system.exceptionHandling;

public class StationNotExistException extends Exception{
    public StationNotExistException(String s)
    {
        super(s);
    }
}
