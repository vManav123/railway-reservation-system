package railway.application.system.exceptionHandling;

public class AccountNoNotExistException extends Exception{
    public AccountNoNotExistException(String s)
    {
        super(s);
    }
}
