package app.core.essential;


import java.util.Iterator;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */


public abstract class AbstractHttpHandler implements HttpHandler {
    private HttpInitializer init = new HttpInitializer();


    @Override
    public void init(HttpInitializer initializer) {
        this.init = initializer;
    }


    public Iterator<String> getInitParameterNames() {
        return this.init.getInitParameterNames();
    }

    public String getInitParameter(String key) {
        return this.init.getInitParameter(key);
    }

}

