package mediatr;

import java.util.List;

public interface RequestHandlerFactory {
    public <TRequest extends Request<TResponse>, TResponse> RequestHandler<TRequest, TResponse> getHandler(Class<TRequest> requestClass);

    public <TRequest extends Request<TResponse>, TResponse> List<RequestHandler<TRequest, TResponse>> getHandlers(Class<TRequest> requestClass); 
}
