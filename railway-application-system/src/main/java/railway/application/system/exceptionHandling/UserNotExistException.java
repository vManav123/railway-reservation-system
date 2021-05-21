package railway.application.system.exceptionHandling;

public class UserNotExistException extends Exception{
    public UserNotExistException(String s)
    {
        super(s);
    }
}
