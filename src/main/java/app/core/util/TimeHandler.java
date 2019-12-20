package app.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */


public class TimeHandler {

    public static String getSimpleDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Instant.now().toEpochMilli());
    }

    public static String getSimpleDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Instant.now().toEpochMilli());
    }

    public static Instant getInstantTime() {
        return Instant.now();
    }

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    public static LocalTime getLocalTime() {
        return LocalTime.now();
    }

    public static Date getDate() {
        return new Date();
    }


    public static Date getFormat(String value) throws ParseException {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(value);
    }


}
