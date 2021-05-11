package railway.application.system.ExceptionHandling;

public class SeatLimitOutOfBoundException extends Exception{
    public SeatLimitOutOfBoundException(String s){
        super(s);
    }
}
