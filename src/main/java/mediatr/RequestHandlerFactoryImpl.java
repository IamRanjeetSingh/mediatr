package mediatr;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import mediatr.exceptions.RequestHandlerNotFoundException;

class RequestHandlerFactoryImpl implements RequestHandlerFactory {

    private Map<Class<? extends Request<?>>, List<Supplier<? extends RequestHandler<? extends Request<?>,?>>>> requestToHandlerMapping;

    RequestHandlerFactoryImpl(
        Map<Class<? extends Request<?>>, List<Supplier<? extends RequestHandler<? extends Request<?>,?>>>> requestToHandlerMapping) {
        this.requestToHandlerMapping = requestToHandlerMapping;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResponse>, TResponse> RequestHandler<TRequest, TResponse> getHandler(
        Class<TRequest> requestClass) {
        Supplier<? extends RequestHandler<? extends Request<?>,?>> firstHandlerSupplier = getFirstHandlerSupplier(requestClass);
        RequestHandler<? extends Request<?>,?> handler = firstHandlerSupplier.get();
        return (RequestHandler<TRequest,TResponse>)handler;
    }

    private <TRequest extends Request<?>> Supplier<? extends RequestHandler<? extends Request<?>,?>> getFirstHandlerSupplier(Class<TRequest> requestClass) {
        List<Supplier<? extends RequestHandler<? extends Request<?>,?>>> handlerSuppliers = getAllHandlerSuppliers(requestClass);
        Supplier<? extends RequestHandler<? extends Request<?>,?>> firstHandlerSupplier = handlerSuppliers.get(0);
        return firstHandlerSupplier;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResponse>, TResponse> List<RequestHandler<TRequest, TResponse>> getHandlers(
        Class<TRequest> requestClass) {
        List<Supplier<? extends RequestHandler<? extends Request<?>,?>>> handlerSuppliers = getAllHandlerSuppliers(requestClass);
        List<RequestHandler<TRequest,TResponse>> handlers = handlerSuppliers.stream()
            .map(handlerSupplier -> handlerSupplier.get())
            .map(handler -> (RequestHandler<TRequest,TResponse>)handler)
            .toList();
        return handlers;
    }

    private List<Supplier<? extends RequestHandler<? extends Request<?>,?>>> getAllHandlerSuppliers(Class<? extends Request<?>> requestClass) {
        List<Supplier<? extends RequestHandler<? extends Request<?>,?>>> handlerSuppliers = requestToHandlerMapping.getOrDefault(requestClass, null);
        if(handlerSuppliers == null)
            throw new RequestHandlerNotFoundException(requestClass);
        return handlerSuppliers;
    }
}
