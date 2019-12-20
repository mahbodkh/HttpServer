package app.core.log;

import app.core.util.TimeHandler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static app.core.config.ConfigLoader.load;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */

public class Logger {

    private BlockingQueue<String> queue;
    private static Logger ourInstance = new Logger();
    private static String className;
    private boolean writeToFile = false;

    public static Logger getLogger(Class clazz) {
        className = clazz.getSimpleName();
        return ourInstance;
    }


    private Logger() {
        writeToFile = getLoggerConfig();
        if (writeToFile) {
            queue = new ArrayBlockingQueue<>(4096);
            new Thread(new LogfileWriter(queue)).start();
        }
    }


    public void info(String message) {
        this.write(LogTypes.INFO.name(), message);
    }


    public void debug(String message) {
        this.write(LogTypes.DEBUG.name(), message);
    }

    public void warn(String message) {
        this.write(LogTypes.WARN.name(), message);
    }

    public void error(String message) {
        this.write(LogTypes.ERROR.name(), message);
    }


    private boolean getLoggerConfig() {
        return Boolean.parseBoolean(load().getLogPolicy());
    }

    private void write(String tag, String message) {
        // Build up log-entry
        String out =
                "[" + TimeHandler.getSimpleDateTime() + "]" + "\t" +
                        "[" + tag + "]" + " " +
                        "[" + className + "]" + " " +
                        "[" + message + "]";

        // Print on console
        System.out.println(out);

        // Write into file
        if (writeToFile) {
            try {
                queue.offer(out, 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
