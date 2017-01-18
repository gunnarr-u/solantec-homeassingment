package com.solanteq.assingment.dataprocessors;

import com.solanteq.assingment.Flag;
import com.solanteq.assingment.services.Broker;
import com.solanteq.assingment.services.RecordGroupingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Roman_Mashchenko on 1/17/2017.
 */
public class DataExtractorImpl extends AbstractDataHandler {

    RecordGroupingService<int[]> recordGroupingService;

    public DataExtractorImpl(RecordGroupingService<int[]> recordGroupingService, Broker broker, int chunkSize) {
        super(broker);
        this.recordGroupingService = recordGroupingService;
        this.chunkSize = chunkSize;
    }

    private int chunkSize;

    @Override
    public void doHandle() {
        Collection<int[]> groupedRecord;
        List<Collection<int[]>> chunk = new ArrayList<>(chunkSize);
        try {
            while (!Thread.currentThread().isInterrupted() && null != (groupedRecord = recordGroupingService.group()) && groupedRecord.size() > 0) {
                chunk.add(groupedRecord);
                if (chunk.size() == chunkSize) {
                    broker.send("extractedData", chunk);
                    broker.updateCounter("extractedLines", chunk.size());
                    chunk = new ArrayList<>(chunkSize);
                }
            }
            if (chunk.size() > 0) {
                ((ArrayList)chunk).trimToSize();
                broker.send("extractedData", chunk);
                broker.updateCounter("extractedLines", chunk.size());
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        } finally {
            broker.setFlag(Flag.EXTRACTION_FINISHED);
        }

    }

}
