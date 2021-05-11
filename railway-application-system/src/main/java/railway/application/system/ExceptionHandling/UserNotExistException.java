package railway.application.system.ExceptionHandling;

public class UserNotExistException extends Exception{
    public UserNotExistException(String s)
    {
        super(s);
    }
}
