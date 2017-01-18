package com.solanteq.assingment.services.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;
import com.solanteq.assingment.services.Broker;

import java.util.concurrent.TimeUnit;

/**
 * Created by Roman_Mashchenko on 1/17/2017.
 */
public class HazelcastBroker implements Broker{

    private HazelcastInstance hazelcast;
    private ISet<Object> flags;

    public HazelcastBroker(HazelcastInstance hazelcast) {
        this.hazelcast = hazelcast;
        flags = hazelcast.getSet("flags");
    }


    @Override
    public void send(String queueName, Object payload) {
        try {

            hazelcast.getQueue(queueName).put(payload);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Object receive(String queueName) throws InterruptedException {
        return  hazelcast.getQueue(queueName).take();
    }

    @Override
    public <T> T receive(String queueName, Class<T> clazz) throws InterruptedException {
        return (T) hazelcast.getQueue(queueName).take();
    }

    @Override
    public <T> T receive(String queueName, Class<T> clazz, long timeout) throws InterruptedException {
        return (T) hazelcast.getQueue(queueName).poll(timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public long updateCounter(String counterName, long value) {
        return hazelcast.getAtomicLong(counterName).addAndGet(value);
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
        return hazelcast.getAtomicLong(counterName).get();
    }
}
