package app.core.log;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public enum LogTypes {

    INFO(0),
    DEBUG(1),
    WARN(2),
    ERROR(3),
    ;


    private final int log;

    LogTypes(int log) {
        this.log = log;
    }
}
