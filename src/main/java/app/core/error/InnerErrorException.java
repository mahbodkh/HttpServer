package app.core.error;

import app.core.http.enums.StatusCodeTypes;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */


public class InnerErrorException extends RuntimeException {
    public InnerErrorException() {
        super(StatusCodeTypes._500.name());
    }
}
