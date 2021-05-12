package railway.application.system.ExceptionHandling;

public class TicketLimitOutOfBoundException extends Exception{
    public TicketLimitOutOfBoundException(String s)
    {
        super(s);
    }
}
