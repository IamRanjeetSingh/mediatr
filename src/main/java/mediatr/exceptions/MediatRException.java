package mediatr.exceptions;

public class MediatRException extends RuntimeException {
    public MediatRException(String message) {
        super(message);
    }

    public MediatRException(String message, Exception innerException) {
        super(message, innerException);
    }
}
