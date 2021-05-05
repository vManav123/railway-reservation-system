package user.management.system.ExceptionHandling;

public class UserNotExistException extends Exception{
    public UserNotExistException(String s)
    {
        super(s);
    }
}
