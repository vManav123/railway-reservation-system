package railway.application.system.exceptionHandling;

public class InvalidQuotaException extends Exception{
    public InvalidQuotaException(String s)
    {
        super(s);
    }
}
