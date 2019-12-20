package app.core.essential;

import app.core.log.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */

@SuppressWarnings("unused")
public class ThreadPoolHandler {
    private static final Logger log = Logger.getLogger(ThreadPoolHandler.class);
    //Thread pool size
    private final int poolSize;

    //Internally pool is an array
    private final WorkerThread[] workers;

    // FIFO ordering
    private final LinkedBlockingQueue<Runnable> queue;

    public ThreadPoolHandler(int poolSize) {
        this.poolSize = poolSize;
        queue = new LinkedBlockingQueue<Runnable>();
        workers = new WorkerThread[poolSize];

        for (int i = 0; i < poolSize; i++) {
            workers[i] = new WorkerThread();
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
    }

    int getPoolSize() {
        return poolSize;
    }

    private class WorkerThread extends Thread {
        public void run() {
            Runnable task;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        try {
                            log.warn(String.format("queue size: '%d'", queue.size()));
                            queue.wait();
                        } catch (InterruptedException e) {
                            log.error(String.format("An error occurred while queue is waiting: {%s}", e.getMessage()));
                        }
                    }
                    task = (Runnable) queue.poll();
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    log.error(String.format("Thread pool is interrupted due to an issue: {%s}", e.getMessage()));
                }
            }
        }
    }

    public void shutdown() {
        log.info("Shutting down thread pool");
        for (int i = 0; i < poolSize; i++) {
            workers[i] = null;
        }
    }
}