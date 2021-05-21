package railway.application.system.exceptionHandling;

public class InvalidContactNumberException extends Exception{
    public InvalidContactNumberException(String s)
    {
        super(s);
    }
}
