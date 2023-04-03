package com.example.minitaxiandroid.mythreads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class MyThread implements Runnable {

    private Thread worker;
    private String name;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicBoolean stopped = new AtomicBoolean(false);
    private int interval;

    public MyThread(int sleepInterval, String name) {
        interval = sleepInterval;
        this.name = name;
    }

    public void start() {
        worker = new Thread(this, name);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    public void interrupt() {
        running.set(false);
        worker.interrupt();
    }

    boolean isRunning() {
        return running.get();
    }

    boolean isStopped() {
        return stopped.get();
    }

    public abstract void run();
}

