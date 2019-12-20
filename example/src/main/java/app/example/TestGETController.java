package app.example;

import app.core.anotation.Controller;
import app.core.anotation.InitParameter;
import app.core.essential.AbstractHttpHandler;
import app.core.http.HttpRequest;
import app.core.http.HttpResponse;
import app.core.http.enums.MethodTypes;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

@Controller(method = MethodTypes.GET, urlPatten = "/name", initParameters = {
        @InitParameter(key = "name", value = "Ebrahim"),
        @InitParameter(key = "family", value = "Kh")
})
public class TestGETController extends AbstractHttpHandler {

    @Override
    public void accept(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        Map<String, String> params = httpRequest.getParams();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
        Iterator<String> iterator = getInitParameterNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            System.out.println(key + ":" + getInitParameter(key));
        }
        char[] chars = {'h', 'e', 'l', 'l', 'o', ',', 'w', 'o', 'r', 'l', 'd', '!'};
        Writer writer = httpResponse.getWrite();
        writer.write(chars);
        writer.flush();
    }

    @Override
    public void destroy() {

    }
}