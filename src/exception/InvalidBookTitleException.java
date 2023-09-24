package exception;

public class InvalidBookTitleException extends Exception{
    public InvalidBookTitleException(String message){
        super(message);
    }
}
