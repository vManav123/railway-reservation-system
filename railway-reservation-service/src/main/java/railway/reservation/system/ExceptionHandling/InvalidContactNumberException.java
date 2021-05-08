package railway.reservation.system.ExceptionHandling;

public class InvalidContactNumberException extends Exception{
    public InvalidContactNumberException(String s)
    {
        super(s);
    }
}
