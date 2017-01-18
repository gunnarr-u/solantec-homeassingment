package com.solanteq.assingment.dataprocessors;

import com.solanteq.assingment.Flag;
import com.solanteq.assingment.functions.BusinessFunction;
import com.solanteq.assingment.services.Broker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Created by Roman_Mashchenko on 1/18/2017.
 */
public class F1F2DataProcessor extends AbstractDataHandler{

    private BusinessFunction<Integer, Integer> f1;
    private BusinessFunction<Integer, Integer> f2;
    private static  final Logger log = LoggerFactory.getLogger(F1F2DataProcessor.class);
    private long counter;

    public F1F2DataProcessor(Broker broker, BusinessFunction<Integer, Integer> f1, BusinessFunction<Integer, Integer> f2) {
        super(broker);
        this.f1 = f1;
        this.f2 = f2;
    }


    @Override
    public void doHandle() throws InterruptedException {
        log.info("Data processor started");
        while (!Thread.currentThread().isInterrupted() && !stopProcessing()) {
            Collection<Collection<int[]>> data = broker.receive("extractedData", Collection.class, 10000);
            if(data == null) continue;
            data.stream()
                    .map(it->it.stream().map(args-> f1.apply(args[0], args[1])).collect(toList()))
                    .map(args->f2.apply(f2.apply(args.get(0), args.get(1)), args.get(2)))
                    .forEach(result->broker.send("processedData", result));
            log.info("Processed {} lines", data.size());
            broker.updateCounter("processedLines", data.size());
            counter += data.size();
        }
        log.info("Data processor finished execution, lines processed by current processor: {} ,total lines processed: {}", counter, broker.getCounterValue("processedLines"));
    }

    private boolean stopProcessing() {
        return broker.checkIfFlagSet(Flag.EXTRACTION_FINISHED)
                && broker.getCounterValue("processedLines") == broker.getCounterValue("extractedLines");
    }

}
