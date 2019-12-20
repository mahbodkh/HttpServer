package app.core.essential;


import app.core.http.HttpRequest;
import app.core.http.HttpResponse;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public interface HttpHandler {
    void init(HttpInitializer initializer);

    void accept(HttpRequest request, HttpResponse response) throws Exception;

    void destroy();
}
