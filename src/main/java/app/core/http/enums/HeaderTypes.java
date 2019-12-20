package app.core.http.enums;

/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */

public enum HeaderTypes {

    JSON("JSON"),
    XML("XML"),
    XFORM("application/x-www-form-urlencoded"),
    UNDEFINED(""),

    ;

    private final String extension;

    HeaderTypes(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        switch (this) {
            case JSON:
                return "Content-type: application/json";
            case XML:
                return "Content-type: application/xml";
            case XFORM:
                return "application/x-www-form-urlencoded";
            default:
                return "UNDEFINED";
        }
    }
}
