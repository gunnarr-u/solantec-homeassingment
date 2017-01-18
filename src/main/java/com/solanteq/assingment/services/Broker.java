package com.solanteq.assingment.services;

/**
 * Created by Roman_Mashchenko on 1/17/2017.
 */
public interface Broker {

    void send(String queueName, Object payload);
    Object receive(String queueName) throws InterruptedException;
    <T> T receive(String queueName, Class<T> clazz) throws InterruptedException;
    <T> T receive(String queueName, Class<T> clazz, long timeout) throws InterruptedException;
    long updateCounter(String counterName, long value);
    void setFlag(String flag);
    boolean checkIfFlagSet(String flag);
    long getCounterValue(String counterName);
}
