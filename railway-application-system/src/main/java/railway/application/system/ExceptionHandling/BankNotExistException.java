package railway.application.system.ExceptionHandling;

public class BankNotExistException extends Exception{
    public BankNotExistException(String s)
    {
        super(s);
    }
}
