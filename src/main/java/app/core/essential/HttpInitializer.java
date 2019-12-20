package app.core.essential;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */


public class HttpInitializer {
    private Map<String, String> initParameter = new ConcurrentHashMap<>();

    public Iterator<String> getInitParameterNames() {
        return initParameter.keySet().iterator();
    }

    public String getInitParameter(String key) {
        return initParameter.get(key);
    }

    public void addInitParameter(String key, String value) {
        this.initParameter.put(key, value);
    }

    public void putAllInitParameter(Map<String, String> map) {
        this.initParameter.putAll(map);
    }
}
