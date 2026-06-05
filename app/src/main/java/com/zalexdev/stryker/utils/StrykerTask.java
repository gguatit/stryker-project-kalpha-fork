package com.zalexdev.stryker.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class StrykerTask<Progress, Result> {
    private static final ExecutorService EXECUTOR =
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Handler MAIN = new Handler(Looper.getMainLooper());

    protected abstract Result doInBackground() throws Exception;

    protected void onProgress(Progress value) {}
    protected void onPostExecute(Result result) {}

    public void publish(Progress value) {
        MAIN.post(() -> onProgress(value));
    }

    public Future<Result> execute() {
        return EXECUTOR.submit(() -> {
            Result result = doInBackground();
            MAIN.post(() -> onPostExecute(result));
            return result;
        });
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}
