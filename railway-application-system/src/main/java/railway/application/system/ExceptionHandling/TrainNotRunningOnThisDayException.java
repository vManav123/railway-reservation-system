package railway.application.system.ExceptionHandling;

public class TrainNotRunningOnThisDayException extends Exception{
    public TrainNotRunningOnThisDayException(String s)
    {
        super(s);
    }
}
