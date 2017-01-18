package com.solanteq.assingment.dataprocessors;

import com.solanteq.assingment.services.Broker;
import org.springframework.context.SmartLifecycle;

/**
 * Created by Roman_Mashchenko on 1/18/2017.
 */
public abstract class  AbstractDataHandler implements SmartLifecycle{
    private Thread workingThread = new Thread(this::handle);
    protected Broker broker;
    private Runnable dataHadlerExecutionListener;

    public  void setListenerCallback(Runnable callback) {
        dataHadlerExecutionListener = callback;
    }

    public AbstractDataHandler(Broker broker) {
        this.broker = broker;
    }

    protected abstract void doHandle() throws InterruptedException;

    private void handle() {
        try {
            doHandle();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            notifyListener();
        }
    }

    private void notifyListener() {
        if (dataHadlerExecutionListener != null) {
            dataHadlerExecutionListener.run();
        }
    }

    @Override
    public void start() {
        if (Thread.State.NEW.equals(workingThread.getState())) {
            workingThread.start();
        }
    }

    @Override
    public void stop() {
        workingThread.interrupt();
    }

    @Override
    public boolean isRunning() {
        return workingThread.isAlive();
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        workingThread.interrupt();
        callback.run();
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
