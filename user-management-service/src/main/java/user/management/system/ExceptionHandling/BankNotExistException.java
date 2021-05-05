package user.management.system.ExceptionHandling;

public class BankNotExistException extends Exception{
    public BankNotExistException(String s)
    {
        super(s);
    }
}
