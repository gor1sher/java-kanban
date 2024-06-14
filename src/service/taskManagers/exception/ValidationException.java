package service.taskManagers.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String msg) {
        super(msg);
    }
}
