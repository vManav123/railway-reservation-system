package railway.reservation.system.ExceptionHandling;

public class InvalidQuotaException extends Exception{
    public InvalidQuotaException(String s)
    {
        super(s);
    }
}
