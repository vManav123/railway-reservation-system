package railway.application.system.exceptionHandling;

public class TrainNotRunningOnThisDayException extends Exception{
    public TrainNotRunningOnThisDayException(String s)
    {
        super(s);
    }
}
