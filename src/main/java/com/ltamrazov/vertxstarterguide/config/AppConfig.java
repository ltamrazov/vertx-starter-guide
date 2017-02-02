package com.ltamrazov.vertxstarterguide.config;

/**
 * Created by levontamrazov on 2017-02-02.
 */
public class AppConfig {
    private int serverThreads;
    private int workerThreads;

    public AppConfig(int serverThreads, int workerThreads) {
        this.serverThreads = serverThreads;
        this.workerThreads = workerThreads;
    }

    public int getServerThreads() {
        return serverThreads;
    }

    public int getWorkerThreads() {
        return workerThreads;
    }
}
