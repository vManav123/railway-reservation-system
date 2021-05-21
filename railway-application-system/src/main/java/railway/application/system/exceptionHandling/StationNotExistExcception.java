package railway.application.system.exceptionHandling;

public class StationNotExistExcception extends Exception{
    public StationNotExistExcception(String s)
    {
        super(s);
    }
}
