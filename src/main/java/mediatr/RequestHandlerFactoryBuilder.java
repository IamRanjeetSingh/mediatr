package mediatr;

import java.util.function.Supplier;

public interface RequestHandlerFactoryBuilder {

    public <TRequest extends Request<TResponse>,TResponse> RequestHandlerFactoryBuilder addRequestHandler(
        Class<TRequest> requestClass, 
        Supplier<? extends RequestHandler<TRequest,TResponse>> handlerSupplier);

    public RequestHandlerFactory build();
}
