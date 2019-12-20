package mock;

import app.core.HttpServerProvider;

public class MockHttpServer {
    public static HttpServerProvider mockServer() {
        return new HttpServerProvider() {
            @Override
            public void run() {
                // do nothing
            }
        };
    }

}