package railway.application.system.ExceptionHandling;

public class StationNotExistException extends Exception{
    public StationNotExistException(String s)
    {
        super(s);
    }
}
