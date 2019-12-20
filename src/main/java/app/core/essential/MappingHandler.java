package app.core.essential;

import app.core.anotation.AnnotationParser;
import app.core.anotation.Controller;
import app.core.error.NoFoundRequestException;
import app.core.http.HttpRequest;
import app.core.http.HttpStaticHandler;
import app.core.http.enums.MethodTypes;
import app.core.log.Logger;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static app.core.config.ConfigLoader.load;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */

public class MappingHandler {
    private static final Logger log = Logger.getLogger(MappingHandler.class);
    private Map<String, HttpHandler> staticHandlerMap = new ConcurrentHashMap<>();
    private Map<MappingInfo, ClassPair<? extends HttpHandler>> handleMap = new ConcurrentHashMap<>();

    public MappingHandler() {
        init();
    }

    public void init() {
        MappingInfo staticMappingInfo = new MappingInfo(MethodTypes.GET, "^/".replaceAll("//", "") + "/(.*)", new HashMap<>());
        HttpInitializer initializer = new HttpInitializer();
        initializer.putAllInitParameter(staticMappingInfo.getInitParameter());
        ClassPair<HttpStaticHandler> staticHandleClassPair = new ClassPair<>(HttpStaticHandler.class, initializer);
        handleMap.put(staticMappingInfo, staticHandleClassPair);


        Reflections reflections = new Reflections(load().getPackageName());
        reflections
                .getTypesAnnotatedWith(Controller.class)
                .forEach(clazz -> {
                    MappingInfo info = AnnotationParser.parseAnnotation(clazz);
                    log.debug(String.format("MappingInfo: { %s } ", info));
                    HttpInitializer init = new HttpInitializer();
                    init.putAllInitParameter(info.getInitParameter());
                    ClassPair<? extends HttpHandler> classPair = new ClassPair<>((Class<? extends HttpHandler>) clazz, init);
                    handleMap.put(info, classPair);
                });
    }

    public HttpHandler getHandler(HttpRequest request) throws Exception {
        String path = request.getPath();
        int index = path.indexOf("?");
        path = path.substring(0, index < 0 ? path.length() : index);
        for (MappingInfo info : this.handleMap.keySet()) {
            Pattern p = Pattern.compile(info.getUrlPatten());
            Matcher matcher = p.matcher(path);
            if (matcher.matches() && info.getMethod() == request.getMethod()) {
                List<String> matcherStrList = new ArrayList<>();
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    matcherStrList.add(matcher.group(i));
                }
                request.addAttributes("matcherStrList", matcherStrList);
                return handleMap.get(info).getInstance();
            }
        }
        log.error(String.format("We cant meet your request call: { %s :%s} ", request.getMethod(), request.getPath()));
        throw new NoFoundRequestException();
    }

    public static class ClassPair<T extends HttpHandler> {
        private Class<T> clazz;
        private T instance;
        private HttpInitializer init;

        public ClassPair(Class<T> clazz, HttpInitializer init) {
            this.clazz = clazz;
            this.init = init;
        }

        public Class<T> getClazz() {
            return clazz;
        }

        public void setClazz(Class<T> clazz) {
            this.clazz = clazz;
        }

        public synchronized T getInstance() throws Exception {
            if (this.instance == null) {
                this.instance = this.clazz.newInstance();
            }
            return this.instance;
        }
    }
}