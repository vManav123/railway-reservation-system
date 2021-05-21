package railway.application.system.exceptionHandling;

public class TicketLimitOutOfBoundException extends Exception{
    public TicketLimitOutOfBoundException(String s)
    {
        super(s);
    }
}
