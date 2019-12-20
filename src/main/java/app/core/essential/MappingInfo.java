package app.core.essential;

import app.core.http.enums.MethodTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */

public class MappingInfo {
    private MethodTypes method;
    private String urlPatten;
    private Map<String, String> initParameter = new HashMap<>();

    public MappingInfo(MethodTypes method, String urlPatten, Map<String, String> initParameter) {
        this.method = method;
        this.urlPatten = urlPatten;
        this.initParameter = initParameter;
    }

    public MethodTypes getMethod() {
        return method;
    }

    public void setMethod(MethodTypes method) {
        this.method = method;
    }

    public String getUrlPatten() {
        return urlPatten;
    }

    public void setUrlPatten(String urlPatten) {
        this.urlPatten = urlPatten;
    }

    public Map<String, String> getInitParameter() {
        return initParameter;
    }

    public void setInitParameter(Map<String, String> initParameter) {
        this.initParameter = initParameter;
    }

    public void addInitParameter(String key, String value) {
        this.initParameter.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MappingInfo that = (MappingInfo) o;

        if (method != that.method) return false;
        return urlPatten.equals(that.urlPatten);

    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + urlPatten.hashCode();
        return result;
    }

    @Override
    //todo stringbuilder
    public String toString() {
        return "MappingInfo{" +
                "method=" + method +
                ", urlPatten='" + urlPatten + '\'' +
                ", initParameter=" + initParameter +
                '}';
    }
}