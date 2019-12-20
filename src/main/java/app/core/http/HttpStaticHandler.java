package app.core.http;

import app.core.error.NoFoundRequestException;
import app.core.essential.AbstractHttpHandler;
import app.core.http.enums.StatusCodeTypes;

import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HttpStaticHandler extends AbstractHttpHandler {
    private static final DateFormat formater = new SimpleDateFormat(
            "dd MMM yyyy HH:mm:ss");

    public static final String OK_200 = StatusCodeTypes._200.getCode();
    public static final String NOT_FOUND_404 = StatusCodeTypes._404.getCode();
    public static final String SERVER_ERROR_500 = StatusCodeTypes._500.getCode();
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String GZIP = "gzip";

    private String basePath = "src/repository/static";

    @Override
    public void accept(HttpRequest request, HttpResponse response) throws Exception {
        String path = ((List<String>) request.getAttribute("matcherStrList")).get(0);
        String file;
        int index = path.indexOf("?");
        int index1 = request.getPath().indexOf('?');
        file = path.substring(0, index < 0 ? path.length() : index);
        String dirPath = request.getPath().substring(0, index1 < 0 ? request.getPath().length() : index1);
        if (basePath.endsWith("/"))
            basePath = basePath.substring(0, basePath.length() - 1);
        String filePath = basePath + "/" + file;
        File f = new File(filePath);
        if (!f.exists()) {
            throw new NoFoundRequestException();
        }
        if (f.isDirectory()) {
            if (!dirPath.endsWith("/")) {
                HttpResponse.redirect(response, request.getPath() + "/");
                return;
            }

            StringBuilder buf = new StringBuilder()
                    .append("<!DOCTYPE html>\r\n")
                    .append("<html><head><meta charset='utf-8' /><title>")
                    .append("File list: ")
                    .append(dirPath)
                    .append("</title></head><body>\r\n")
                    .append("<h3>File list: ")
                    .append(dirPath)
                    .append("</h3>\r\n")
                    .append("<ul>")
                    .append("<li><a href=\"../\">..</a></li>\r\n");
            for (File f1 : f.listFiles()) {
                if (f1.isHidden() || !f1.canRead()) {
                    continue;
                }
                String name = f1.getName();
                buf.append("<li><a href=\"")
                        .append(name)
                        .append("\">")
                        .append(name)
                        .append("</a></li>\r\n");
            }

            buf.append("</ul></body></html>\r\n");
            response.addHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
            response.addHeader(CONNECTION, KEEP_ALIVE);
            response.getOutput().write(buf.toString().getBytes("UTF-8"));
            return;
        }
        byte[] body = Files.readAllBytes(f.toPath());
        String mime = f.toURL().openConnection().getContentType();
        response.addHeader(CONTENT_TYPE, mime);
        response.setStatusCode(StatusCodeTypes._200);
        response.setBody("OK");
        Date lastModified = new Date(f.lastModified());
        response.addHeader(LAST_MODIFIED,
                formater.format(lastModified));
        response.addHeader(CONNECTION, KEEP_ALIVE);
        response.getOutput().write(body);
    }

    @Override
    public void destroy() {

    }
}