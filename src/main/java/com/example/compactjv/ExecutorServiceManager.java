package com.example.compactjv;

import java.util.concurrent.*;

public class ExecutorServiceManager {

    private final ExecutorService executorService;
    private ScheduledExecutorService scheduler;

    public ExecutorServiceManager() {
        this.executorService = Executors.newFixedThreadPool(4); // Sesuaikan jumlah thread sesuai dengan kebutuhan
    }

    public void executeTask(Runnable task) {
        executorService.execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                // Handle exception here (log or alert the user)
            }
        });
    }

    public Future<?> submitTask(Runnable task) {
        return executorService.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                // Handle exception here (log or alert the user)
            }
        });
    }

    public ScheduledExecutorService getScheduledExecutor() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        scheduler = Executors.newScheduledThreadPool(1);
        return scheduler;
    }

    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
