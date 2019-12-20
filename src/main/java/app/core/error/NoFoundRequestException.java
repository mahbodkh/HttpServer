package app.core.error;

import app.core.http.enums.StatusCodeTypes;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */

public class NoFoundRequestException extends RuntimeException {
    public NoFoundRequestException() {
        super(StatusCodeTypes._404.name());
    }
}
