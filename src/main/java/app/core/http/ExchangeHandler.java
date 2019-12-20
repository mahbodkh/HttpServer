package app.core.http;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */

import app.core.aop.Filter;
import app.core.error.NoFoundRequestException;
import app.core.http.enums.StatusCodeTypes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExchangeHandler implements Runnable {
    private static final ThreadLocal<DateFormat> formater = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        }
    };
    private HttpRequest request;
    private HttpResponse response;
    private SelectionKey key;

    private Filter filter;
    private Selector selector;

    public ExchangeHandler() {
    }

    public ExchangeHandler(HttpRequest request, HttpResponse response, SelectionKey key, Filter filter, Selector selector) {
        this.request = request;
        this.response = response;
        this.key = key;
        this.filter = filter;
        this.selector = selector;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public SelectionKey getKey() {
        return key;
    }

    public void setKey(SelectionKey key) {
        this.key = key;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            filter.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (e instanceof NoFoundRequestException) {
                    response.setStatusCode(StatusCodeTypes._404);
                    response.setBody("Not Found");
                    response.getOutput().write(StatusCodeTypes._404.name().getBytes(Charset.forName("ISO-8859-1")));
                } else {
                    response.setStatusCode(StatusCodeTypes._500);
                    response.setBody("Internal Server Error");
                    response.getOutput().write(StatusCodeTypes._500.name().getBytes(Charset.forName("ISO-8859-1")));
                }
            } catch (IOException e1) {
            }
        }
        response.addHeader("Date", formater.get().format(new Date()));
        byte[] bodyBytes = response.getOutput().toByteArray();
        response.addHeader("Content-Length", Integer.toString(bodyBytes.length));
        byte[] headerBytes = response.headersToBytes();
        ByteBuffer responseBuffer = ByteBuffer.allocate(headerBytes.length + bodyBytes.length);
        responseBuffer.put(headerBytes).put(bodyBytes);
        responseBuffer.flip();
        key.attach(responseBuffer);
        key.interestOps(SelectionKey.OP_WRITE);
        selector.wakeup();
    }
}
