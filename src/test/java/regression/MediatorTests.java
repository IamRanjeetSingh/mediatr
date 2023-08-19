package regression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mediatr.Mediator;
import mediatr.MediatorImpl;
import mediatr.Request;
import mediatr.RequestHandler;
import mediatr.RequestHandlerFactory;
import mediatr.RequestHandlerFactoryBuilder;
import mediatr.RequestHandlerFactoryBuilderImpl;
import mediatr.exceptions.RequestHandlerNotFoundException;

public class MediatorTests {

    @Test
    public void send_handlerFound_handlerHandle() {
        Mediator mediator = Helper.createMediator(DummyRequest.class, new DummyRequestHandler());
        DummyRequest dummyRequest = new DummyRequest();
        
        mediator.send(dummyRequest);

        assertTrue(
            "Request not handled by handler.", 
            dummyRequest.isHandledAtleastOnce());
    }

    @Test
    public void send_handlerNotFound_throwRequestHandlerNotFoundException() {
        Mediator mediator = Helper.createMediator();
        DummyRequest dummyRequest = new DummyRequest();

        assertThrows(
            "RequestHandlerNotFoundException was not thrown.", 
            RequestHandlerNotFoundException.class, 
            () -> mediator.send(dummyRequest));
    }

    @Test
    public void publish_allHandlerFound_allHandlersHandle() {
        List<DummyRequestHandler> handlers = new ArrayList<>();
        handlers.add(new DummyRequestHandler());
        handlers.add(new DummyRequestHandler());
        handlers.add(new DummyRequestHandler());
        RequestHandlerFactoryBuilder requestHandlerFactoryBuilder = Helper.createRequestHandlerFactoryBuilder();
        handlers.forEach(handler -> requestHandlerFactoryBuilder.addRequestHandler(DummyRequest.class, () -> handler));
        Mediator mediator = Helper.createMediator(requestHandlerFactoryBuilder.build());
        DummyRequest dummyRequest = new DummyRequest();
        
        mediator.publish(dummyRequest);
        
        int expectedRequestHandleCount = handlers.size();
        int actualRequestHandleCount = dummyRequest.getHandleCount();
        assertEquals(
            "Request not handled by expected number of handlers.", 
            expectedRequestHandleCount, 
            actualRequestHandleCount);
    }
    
    private static class DummyRequest implements Request<Void> {
        private int handleCount = 0;

        public void handle() {
            handleCount++;
        }

        public int getHandleCount() {
            return handleCount;
        }

        public boolean isHandledAtleastOnce() {
            return handleCount > 0;
        }
    }

    private static class DummyRequestHandler implements RequestHandler<DummyRequest,Void> {

        @Override
        public Void handle(DummyRequest request) {
            request.handle();
            return null;
        }
    }

    private static class Helper {
        private static <TRequest extends Request<TResponse>,TResponse> Mediator createMediator(
            Class<TRequest> requestClass, 
            RequestHandler<TRequest,TResponse> handler) {
            return createMediator(createRequestHandlerFactoryBuilder()
                .addRequestHandler(requestClass, () -> handler)
                .build());
        }

        private static Mediator createMediator() {
            return createMediator(createRequestHandlerFactoryBuilder()
                .build());
        }

        private static Mediator createMediator(RequestHandlerFactory requestHandlerFactory) {
            return new MediatorImpl(requestHandlerFactory);
        }

        private static RequestHandlerFactoryBuilder createRequestHandlerFactoryBuilder() {
            return new RequestHandlerFactoryBuilderImpl();
        }
    }
}
