package app.core.util;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class PortHandler {

    public static int getValidPortParam(String value) throws NumberFormatException {
        int port = Integer.parseInt(value);
        if (port > 0 && port < 65535) {
            return port;
        } else {
            throw new NumberFormatException("Invalid port! Port value is a number between 0 and 65535");
        }
    }


}
