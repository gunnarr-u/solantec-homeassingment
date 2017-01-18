package com.solanteq.assingment.services.impl;

import com.solanteq.assingment.services.Broker;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Roman_Mashchenko on 1/17/2017.
 */
public class LocalBroker implements Broker {

    HashMap<String, AtomicLong> countersMap = new HashMap();

    HashMap<String, BlockingQueue> queueMap = new HashMap<>();
    HashSet<String> flags = new HashSet<>();

    @PostConstruct
    public void init() {
        queueMap.put("extractedData", new LinkedBlockingDeque());
        queueMap.put("processedData", new LinkedBlockingDeque());
        countersMap.put("extractedLines", new AtomicLong(0));
        countersMap.put("processedLines", new AtomicLong(0));
    }

    @Override
    public void send(String queueName, Object payload) {
        queueMap.get(queueName).offer(payload);
    }

    @Override
    public Object receive(String queueName) throws InterruptedException {
        return queueMap.get(queueName).take();
    }

    @Override
    public <T> T receive(String queueName, Class<T> clazz) throws InterruptedException {
        return (T) receive(queueName);
    }

    @Override
    public <T> T receive(String queueName, Class<T> clazz, long timeout) throws InterruptedException {
        return (T) queueMap.get(queueName).poll(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public long updateCounter(String counterName, long value) {
       return countersMap.get(counterName).addAndGet(value);
    }

    @Override
    public void setFlag(String flag) {
       flags.add(flag);
    }

    @Override
    public boolean checkIfFlagSet(String flag) {
        return flags.contains(flag);
    }

    @Override
    public long getCounterValue(String counterName) {
        return countersMap.get(counterName).get();
    }
}
