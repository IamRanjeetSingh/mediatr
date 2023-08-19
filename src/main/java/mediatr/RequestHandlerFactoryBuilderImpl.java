package mediatr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RequestHandlerFactoryBuilderImpl implements RequestHandlerFactoryBuilder {
    private Map<Class<? extends Request<?>>, List<Supplier<? extends RequestHandler<? extends Request<?>,?>>>> requestToHandlerMapping;

    public RequestHandlerFactoryBuilderImpl() { 
        requestToHandlerMapping = new HashMap<>();
    }

    public <TRequest extends Request<TResponse>,TResponse> RequestHandlerFactoryBuilder addRequestHandler(
        Class<TRequest> requestClass, 
        Supplier<? extends RequestHandler<TRequest,TResponse>> handlerSupplier) {
        if(!requestToHandlerMapping.containsKey(requestClass))
            requestToHandlerMapping.put(requestClass, new ArrayList<>());
        requestToHandlerMapping.get(requestClass).add(handlerSupplier);
        return this;
    }

    public RequestHandlerFactory build() {
        return new RequestHandlerFactoryImpl(requestToHandlerMapping);
    }
}
