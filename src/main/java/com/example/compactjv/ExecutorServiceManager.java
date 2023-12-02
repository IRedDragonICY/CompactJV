package com.example.compactjv;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceManager {

    private ExecutorService executorService;

    public ExecutorServiceManager() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    public void executeTask(Runnable task) {
        this.executorService.execute(task);
    }
}
