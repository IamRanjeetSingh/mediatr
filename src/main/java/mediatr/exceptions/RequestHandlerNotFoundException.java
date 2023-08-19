package mediatr.exceptions;

import mediatr.Request;
import mediatr.RequestHandler;

public class RequestHandlerNotFoundException extends MediatRException {

    public RequestHandlerNotFoundException(Class<? extends Request<?>> requestClass) {
        super(String.format("No {requestHandlerClassName} found for request {requestClassName}", 
            RequestHandler.class.getName(), 
            requestClass.getName()));
    }
    
}
