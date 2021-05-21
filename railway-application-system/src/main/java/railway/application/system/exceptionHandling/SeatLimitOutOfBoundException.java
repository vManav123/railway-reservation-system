package railway.application.system.exceptionHandling;

public class SeatLimitOutOfBoundException extends Exception{
    public SeatLimitOutOfBoundException(String s){
        super(s);
    }
}
