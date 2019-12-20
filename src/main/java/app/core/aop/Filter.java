package app.core.aop;

import app.core.essential.HttpSessionManager;
import app.core.essential.MappingHandler;
import app.core.http.HttpContext;
import app.core.essential.HttpHandler;
import app.core.http.HttpRequest;
import app.core.http.HttpResponse;
import app.core.log.Logger;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class Filter {
    private final static Logger log = Logger.getLogger(Filter.class);
    private HttpSessionManager sessionManager;
    private MappingHandler mappingHandler;

    public Filter() {
        this.sessionManager = new HttpSessionManager();
        this.mappingHandler = new MappingHandler();
    }

    public void doFilter(HttpRequest request, HttpResponse response) throws Exception {
        log.info(String.format("doFilter method call  | request: {%s} | response: {%s} ", request.toString(), response.toString()));

        HttpContext context = new HttpContext(request, response, this.getSessionManager());
        request.setContext(context);
        response.setContext(context);
        response.setRequest(request);
        HttpHandler handle = mappingHandler.getHandler(request);
        handle.accept(request, response);
    }

    public HttpSessionManager getSessionManager() {
        return sessionManager;
    }
}
