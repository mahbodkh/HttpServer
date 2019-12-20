package app.core.http.enums;

/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */

public enum MethodTypes {

    POST("POST") {
        @Override public boolean isPOST() { return true; }
    },
    GET("GET") {
        @Override public boolean isGET() { return true; }
    },
    PUT("PUT") {
        @Override public boolean isPUT() { return true; }
    },
    PATCH("PATCH") {
        @Override public boolean isPATCH() { return true; }
    },
    DELETE("DELETE") {
        @Override public boolean isDELETE() { return true; }
    },
    HEAD("HEAD") {
        @Override public boolean isHEAD() { return true; }
    },
    TRACE("TRACE") {
        @Override public boolean isTRACE() { return true; }
    },
    OPTIONS("OPTIONS") {
        @Override public boolean isOPTIONS() { return true; }
    },
    CONNECT("CONNECT") {
        @Override public boolean isCONNECT() { return true; }
    };

    public static MethodTypes from(final String value) {
        switch (value) {
            case "POST": return POST;
            case "GET": return GET;
            case "PUT": return PUT;
            case "PATCH":return PATCH;
            case "DELETE": return DELETE;
            case "HEAD": return HEAD;
            case "TRACE": return TRACE;
            case "OPTIONS": return OPTIONS;
            case "CONNECT": return CONNECT;
            default:
                throw new IllegalArgumentException(StatusCodeTypes._405 + "\n\n" + value);
        }
    }

    public final String name;

    private MethodTypes(final String name) {
        this.name = name;
    }


    public boolean isCONNECT() { return false; }
    public boolean isDELETE() { return false; }
    public boolean isGET() { return false; }
    public boolean isHEAD() { return false; }
    public boolean isOPTIONS() { return false; }
    public boolean isPATCH() { return false; }
    public boolean isPOST() { return false; }
    public boolean isPUT() { return false; }
    public boolean isTRACE() { return false; }

}
