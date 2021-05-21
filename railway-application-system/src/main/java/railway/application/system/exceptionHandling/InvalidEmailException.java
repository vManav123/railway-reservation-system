package railway.application.system.exceptionHandling;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String s) {
        super(s);
    }
}
