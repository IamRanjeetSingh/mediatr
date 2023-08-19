package mediatr;

public interface Mediator {
    public <TRequest extends Request<TResponse>, TResponse> TResponse send(TRequest request);
    public <TRequest extends Request<TResponse>, TResponse> void publish(TRequest request);
}
