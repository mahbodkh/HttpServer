package app.core.http;

import app.core.http.enums.HeaderTypes;
import app.core.http.enums.HttpVersionTypes;
import app.core.http.enums.StatusCodeTypes;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class HttpResponse implements Serializable {

    private HeaderTypes header;
    private HttpVersionTypes httpVersion;
    private StatusCodeTypes statusCode;
    private String body;
    private List<Header> headers = new LinkedList<>();
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private HttpContext context;
    private HttpRequest request;

    public HttpResponse() {
    }


    public HeaderTypes getHeader() {
        return header;
    }

    public void setHeader(HeaderTypes header) {
        this.header = header;
    }

    public HttpVersionTypes getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersionTypes httpVersion) {
        this.httpVersion = httpVersion;
    }

    public StatusCodeTypes getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCodeTypes statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public HttpResponse addHeader(String value, String header) {
        headers.add(new Header(value, header));
        return this;
    }

    public ByteArrayOutputStream getOutput() {
        return output;
    }

    public void setOutput(ByteArrayOutputStream output) {
        this.output = output;
    }

    public void setContext(HttpContext context) {
        this.context = context;
    }

    public HttpContext getContext() {
        return context;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public byte[] headersToBytes() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.httpVersion).append(" ").append(this.statusCode).append(this.body).append("\r\n");
        for (Header header : this.headers) {
            stringBuilder.append(header.getName()).append(": ").append(header.getValue()).append("\r\n");
        }
        stringBuilder.append("\r\n");
        return stringBuilder.toString().getBytes(StandardCharsets.ISO_8859_1);
    }

    public BufferedWriter getWrite() {
        return new BufferedWriter(new OutputStreamWriter(this.getOutput()));
    }


    public static HttpResponse redirect(HttpResponse response, String newUrl) {
        response.setStatusCode(StatusCodeTypes._302);
        response.setHttpVersion(HttpVersionTypes.Http1_1);
        response.setBody("find it");
        response.addHeader("Location", newUrl);
        return response;
    }
}
