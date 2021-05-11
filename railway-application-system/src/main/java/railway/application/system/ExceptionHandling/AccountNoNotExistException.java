package railway.application.system.ExceptionHandling;

public class AccountNoNotExistException extends Exception{
    public AccountNoNotExistException(String s)
    {
        super(s);
    }
}
