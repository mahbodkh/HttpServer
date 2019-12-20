package app.core.log;

import app.core.util.TimeHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import static app.core.config.ConfigLoader.load;


/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class LogfileWriter implements Runnable {
    private BlockingQueue<String> queue;
    private File logfile;

    public LogfileWriter(BlockingQueue<String> queue) {
        this.queue = queue;
        logfile = new File(load().getLogAddress() + "/" + TimeHandler.getSimpleDate());
    }


    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(logfile, true))) {
                printWriter.append(queue.take()).append("\r\n");
            } catch (IOException | InterruptedException ignored) {

            }
        }
    }
}
