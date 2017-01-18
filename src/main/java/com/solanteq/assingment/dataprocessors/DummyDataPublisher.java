package com.solanteq.assingment.dataprocessors;

import com.solanteq.assingment.services.Broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Roman_Mashchenko on 1/18/2017.
 */
public class DummyDataPublisher extends AbstractDataHandler{
    private static final Logger log = LoggerFactory.getLogger(DummyDataPublisher.class);


    public DummyDataPublisher(Broker broker) {
        super(broker);
    }

    @Override
    public void doHandle() throws InterruptedException{
        while (!Thread.currentThread().isInterrupted()) {
            Integer result = broker.receive("processedData", Integer.class);
            log.info(result.toString());
        }
    }
}
