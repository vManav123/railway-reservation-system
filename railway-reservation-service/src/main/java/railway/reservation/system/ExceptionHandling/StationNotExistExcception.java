package railway.reservation.system.ExceptionHandling;

public class StationNotExistExcception extends Exception{
    public StationNotExistExcception(String s)
    {
        super(s);
    }
}
