package app.core.parser;

import app.core.error.ProtocolSyntaxException;
import app.core.http.HttpRequest;
import app.core.http.enums.HeaderTypes;
import app.core.http.enums.HttpVersionTypes;
import app.core.http.enums.MethodTypes;
import app.core.log.Logger;
import app.core.util.TimeHandler;

import javax.servlet.http.Cookie;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class HttpParser {
    private static final Logger log = Logger.getLogger(HttpParser.class);

    private static final byte[] END = new byte[]{13, 10, 13, 10};
    private boolean isParseRequestHeader = false;
    private boolean isGet = true;
    private boolean isHttp11 = true;
    private boolean isKeepAlive = true;

    private byte[] headerBytes = new byte[0];
    private byte[] bodyBytes = new byte[0];

    private HttpRequest request;

    private SelectionKey key;

    private Map<SocketChannel, HttpParser> map;

    public HttpParser(SelectionKey key, Map<SocketChannel, HttpParser> map) {
        this.key = key;
        this.map = map;
    }

    public boolean parse() throws Exception {
        log.debug("parse method called");
        if (!isParseRequestHeader) {
            byte[] bytes = readBytes();
            if (bytes == null) {
                map.remove(this.key.channel());
                return false;
            }
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            outputStream1.write(headerBytes);
            outputStream1.write(bytes);
            byte[] sourceBytes = outputStream1.toByteArray();
            int index = findIndex(sourceBytes, END);
            if (index < 0) {
                this.headerBytes = sourceBytes;
                return false;
            } else {
                this.isParseRequestHeader = true;
                this.headerBytes = new byte[index];
                System.arraycopy(sourceBytes, 0, this.headerBytes, 0, index);
                parseHeaders();
                if ((sourceBytes.length - index - 4) > 0) {
                    if (isGet) {
                        log.warn("http protocol parse syntax error!");
                        throw new ProtocolSyntaxException("http protocol parse syntax error!");
                    } else {
                        if ((sourceBytes.length - index - 4) >= this.request.getContentLength()) {
                            this.bodyBytes = new byte[(int) this.request.getContentLength()];
                            System.arraycopy(sourceBytes, index + 4, this.bodyBytes, 0, (int) this.request.getContentLength());
                            this.request.setBody(this.bodyBytes);
                            return true;
                        } else {
                            this.bodyBytes = new byte[sourceBytes.length - index - 4];
                            System.arraycopy(sourceBytes, index + 4, this.bodyBytes, 0, sourceBytes.length - index - 4);
                            return false;
                        }
                    }
                } else {
                    if (this.isGet)
                        return true;
                }
            }
        } else {
            byte[] bytes = readBytes();
            if (bytes == null) {
                map.remove(key.channel());
                return false;
            }
            if (bytes.length > 0) {
                if (this.isGet) {
                    throw new ProtocolSyntaxException("http protocol parse syntax error!");
                } else {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(this.bodyBytes);
                    outputStream.write(bytes);
                    byte[] byteArray = outputStream.toByteArray();
                    long length = this.getRequest().getContentLength();
                    if (byteArray.length >= length) {
                        byte[] bytes1 = new byte[(int) length];
                        System.arraycopy(byteArray, 0, bytes1, 0, (int) length);
                        this.bodyBytes = bytes1;
                        this.request.setBody(this.bodyBytes);
                        return true;
                    } else {
                        this.bodyBytes = byteArray;
                    }
                }
            }
        }
        return false;
    }

    private byte[] readBytes() throws IOException {
        log.debug("readBytes method called");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int count = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        do {
            byteBuffer.clear();
            SocketChannel socketChannel = (SocketChannel) this.key.channel();
            count = socketChannel.read(byteBuffer);
            if (count >= 0) {
                byte[] bytes = new byte[count];
                System.arraycopy(byteBuffer.array(), 0, bytes, 0, count);
                outputStream.write(bytes);
            } else {
                this.key.cancel();
                this.key.channel().close();
                return null;
            }
        } while (count > 0);
        return outputStream.toByteArray();
    }

    private void parseHeaders() throws Exception {
        log.debug("parseHeaders called");
        this.request = new HttpRequest();
        String headerStr = new String(this.headerBytes, StandardCharsets.ISO_8859_1);
        String[] headers = headerStr.trim().split("\r\n");
        String[] lines = headers[0].split("\\s+");
        request.setMethod(MethodTypes.valueOf(lines[0].trim()));
        request.setPath(lines[1].trim());
        request.setProtocol(lines[2].trim());
        if (request.getPath().startsWith("http://")) {
            int index1 = request.getPath().indexOf("/", 7);
            if (index1 < 0)
                request.setPath("/");
            else {
                request.setPath(request.getPath().substring(index1));
            }
        }
        for (int i = 1; i < headers.length; i++) {
            String[] headerPair = headers[i].split(":");
            String name = headerPair[0].trim().toLowerCase();
            String value = headerPair[1].trim();
            request.addHeaders(HeaderTypes.valueOf(value));
            switch (name) {
                case "accept":
                    request.setAccept(value);
                    break;
                case "accept-charset":
                    request.setAcceptCharset(value);
                    break;
                case "accept-encoding":
                    request.setAcceptEncoding(value);
                    break;
                case "accept-language":
                    request.setAcceptLanguage(value);
                    break;
                case "connection":
                    request.setConnection(value);
                    break;
                case "cookie":
                    String[] cookieStrs = value.split("; ");
                    for (String cookieStr : cookieStrs) {
                        cookieStr = cookieStr.trim();
                        String[] cookiePair = cookieStr.split("=");
                        String cookieName = cookiePair[0].trim();
                        String cookieValue = cookiePair[1].trim();
                        if ("jsessionid".equalsIgnoreCase(cookieName)) {
                            request.setFirstGetSession(false);
                            request.setSessionId(cookieValue);
                        }
                        request.addCookies(new Cookie(cookieName, cookieValue));
                    }
                    break;
                case "content-length":
                    request.setContentLength(Long.parseLong(value));
                    break;
                case "content-type":
                    request.addHeaders(HeaderTypes.valueOf(value));
                    break;
                case "date":
                    request.setDate(TimeHandler.getFormat(value));
                    break;
                case "host":
                    request.setHost(value);
                    break;
            }
        }
        if (MethodTypes.GET.name.equals(request.getMethod())) {
            this.isGet = true;
        } else {
            this.isGet = false;
        }
        if (request.getProtocol().equalsIgnoreCase(HttpVersionTypes.HTTP_1_1)) {
            this.isHttp11 = true;
            if (request.getConnection().equalsIgnoreCase("keep-alive")) {
                this.isKeepAlive = true;
            } else {
                this.isKeepAlive = false;
            }
        } else {
            this.isHttp11 = false;
            this.isKeepAlive = false;
        }
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public void clear() {
        this.isParseRequestHeader = false;
        this.isGet = true;
        this.isHttp11 = true;
        this.isKeepAlive = true;
        this.headerBytes = new byte[0];
        this.bodyBytes = new byte[0];
        this.request = null;
    }

    private int findIndex(byte[] source, byte[] bytes) {
        log.debug(String.format("findIndex method called | source: {%s} | bytes: {%s} "
                , Arrays.toString(source)
                , Arrays.toString(bytes))
        );
        outor:
        for (int i = 0; i <= source.length - bytes.length; i++) {
            int j;
            for (j = 0; j < bytes.length; j++) {
                if (source[i] == bytes[j]) {
                    i++;
                } else {
                    i -= j;
                    continue outor;
                }
            }
            if (j == bytes.length) {
                return i - j;
            }
        }
        return -1;
    }

}
