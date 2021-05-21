package railway.application.system.exceptionHandling;

public class BankNotExistException extends Exception{
    public BankNotExistException(String s)
    {
        super(s);
    }
}
