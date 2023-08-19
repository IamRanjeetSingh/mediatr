package mediatr;

import java.util.List;

public class MediatorImpl implements Mediator {

    private RequestHandlerFactory requestHandlerFactory;

    public MediatorImpl(RequestHandlerFactory requestHandlerFactory) {
        this.requestHandlerFactory = requestHandlerFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResponse>, TResponse> TResponse send(TRequest request) {
        RequestHandler<TRequest, TResponse> handler = requestHandlerFactory.getHandler(request.getClass());
        return handler.handle(request);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResponse>, TResponse> void publish(TRequest request) {
        List<RequestHandler<TRequest,TResponse>> handlers = requestHandlerFactory.getHandlers(request.getClass()); 
        handlers.forEach(handler -> handler.handle(request));
    }
}
