package railway.management.system.ExceptionHandling;

public class TrainNotRunningOnThisDayException extends Exception{
    public TrainNotRunningOnThisDayException(String s)
    {
        super(s);
    }
}
