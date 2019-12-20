package app.core.http;


import app.core.http.enums.HeaderTypes;
import app.core.http.enums.MethodTypes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class HttpRequest implements Serializable {

    private MethodTypes method;
    private String path;
    private String protocol;

    private String accept;
    private String acceptCharset;
    private String acceptEncoding;
    private String acceptLanguage;
    private String connection;
    private List<Cookie> cookies = new LinkedList<Cookie>();
    private long contentLength = 0;
    private String contentType;
    private Date date;
    private String host;
    private String sessionId;

    private List<HeaderTypes> headers = new LinkedList<HeaderTypes>();
    private byte[] body = new byte[0];
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, Object> attributes = new HashMap<>();
    private HttpSession session;

    private boolean isParseParams = false;
    private boolean isFirstGetSession = true;
    private HttpContext context;

    public MethodTypes getMethod() {
        return method;
    }

    public void setMethod(MethodTypes method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptCharset() {
        return acceptCharset;
    }

    public void setAcceptCharset(String acceptCharset) {
        this.acceptCharset = acceptCharset;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public void addCookies(Cookie cookie) {
        cookies.add(cookie);
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<HeaderTypes> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HeaderTypes> headers) {
        this.headers = headers;
    }

    public void addHeaders(HeaderTypes headerTypes) {
        headers.add(headerTypes);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public boolean isParseParams() {
        return isParseParams;
    }

    public void setParseParams(boolean parseParams) {
        isParseParams = parseParams;
    }

    public boolean isFirstGetSession() {
        return isFirstGetSession;
    }

    public void setFirstGetSession(boolean firstGetSession) {
        isFirstGetSession = firstGetSession;
    }

    private void parseParams() {
        switch (method) {
            case GET:
                int index = this.getPath().indexOf("?");
                int index1 = this.getPath().indexOf("#");
                if (index < 0)
                    return;
                else {
                    String queryStr = this.getPath().substring(index + 1, index1 < 0 ? this.getPath().length() : index1);
                    if (queryStr.length() > 0) {
                        try {
                            queryStr = URLDecoder.decode(queryStr, "UTF-8");
                            String[] paramPairs = queryStr.split("&");
                            for (String paramPair : paramPairs) {
                                String[] pair = paramPair.split("=");
                                this.params.put(pair[0].trim(), pair[1].trim());
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case POST:
                if (HeaderTypes.XFORM.name().equalsIgnoreCase(this.getContentType()) && this.getContentLength() > 0) {
                    try {
                        String queryStr = new String(this.getBody(), "ISO-8859-1");
                        queryStr = URLDecoder.decode(queryStr, "UTF-8");
                        String[] paramPairs = queryStr.split("&");
                        for (String paramPair : paramPairs) {
                            String[] pair = paramPair.split("=");
                            this.params.put(pair[0].trim(), pair[1].trim());
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    public void setContext(HttpContext context) {
        this.context = context;
    }

    public HttpContext getContext() {
        return context;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(String key, Object value) {
        this.attributes.put(key, value);
    }

    public Object getAttribute(String key){
        return this.attributes.get(key);
    }
}
