package user.management.system.ExceptionHandling;

public class InvalidEmailException extends Exception{
    public InvalidEmailException(String s)
    {
        super(s);
    }
}