package railway.application.system.ExceptionHandling;

public class InvalidQuotaException extends Exception{
    public InvalidQuotaException(String s)
    {
        super(s);
    }
}
