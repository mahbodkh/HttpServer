package app.core.anotation;

import app.core.essential.MappingInfo;
import app.core.http.enums.MethodTypes;

import java.util.HashMap;
import java.util.Map;

public class AnnotationParser {
    public static <T> MappingInfo parseAnnotation(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Controller.class)) {
            Controller controller = clazz.getAnnotation(Controller.class);
            MethodTypes method = controller.method();
            String urlPatten = controller.urlPatten();
            InitParameter[] initParameters = controller.initParameters();
            urlPatten.replaceAll("\\*", "(.+)");
            Map<String, String> map = new HashMap<>();
            for (InitParameter initParameter : initParameters) {
                map.put(initParameter.key(), initParameter.value());
            }
            MappingInfo info = new MappingInfo(method, urlPatten, map);
            return info;
        }
        return null;
    }
}