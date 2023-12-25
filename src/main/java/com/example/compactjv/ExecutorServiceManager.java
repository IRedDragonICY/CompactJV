package com.example.compactjv;

import lombok.Getter;

import java.util.concurrent.*;
@Getter
public class ExecutorServiceManager {

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduler;

    public ExecutorServiceManager() {
        this.executorService = Executors.newFixedThreadPool(4);
        this.scheduler = Executors.newScheduledThreadPool(1);
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

    public ScheduledFuture<?> executePeriodicTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public Future<?> submitTask(Runnable task) {
        return executorService.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdownScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
