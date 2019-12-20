package app.core.http;

import app.core.essential.HttpSessionManager;
/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */

public class HttpContext {
    private HttpRequest request;
    private HttpResponse response;
    private HttpSessionManager sessionManager;

    public HttpContext(HttpRequest request, HttpResponse response,HttpSessionManager sessionManager) {
        this.request = request;
        this.response = response;
        this.sessionManager = sessionManager;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public HttpSessionManager getSessionManager() {
        return sessionManager;
    }
}
