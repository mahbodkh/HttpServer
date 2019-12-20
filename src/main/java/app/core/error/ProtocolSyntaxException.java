package app.core.error;

import app.core.log.Logger;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */


public class ProtocolSyntaxException extends RuntimeException {
    private static final Logger log = Logger.getLogger(ProtocolSyntaxException.class);

    public ProtocolSyntaxException(String message) {
        super(message);
        log.error(message);
    }
}
