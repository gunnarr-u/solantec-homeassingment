package com.solanteq.assingment.dataprocessors;

import com.solanteq.assingment.Flag;
import com.solanteq.assingment.services.Broker;
import com.solanteq.assingment.services.WriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by Roman_Mashchenko on 1/18/2017.
 */
public class FileDataPublisher extends AbstractDataHandler {
    private static final Logger log = LoggerFactory.getLogger(FileDataPublisher.class);
    private final WriterService<Integer> fileWriterService;


    public FileDataPublisher(Broker broker, WriterService<Integer> fileWriterService) {
        super(broker);
        this.fileWriterService = fileWriterService;
    }

    @Override
    public void doHandle() throws InterruptedException {
        log.info("Data publisher started");
        try {
        fileWriterService.open();
            while (!Thread.currentThread().isInterrupted() && !stopWriting()) {
                Integer result = broker.receive("processedData", Integer.class, 1000);
                if (result == null) continue;
                fileWriterService.write(Arrays.asList(result));
                broker.updateCounter("writedLines", 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            fileWriterService.close();
        }
        log.info("Data publisher finished. Writed lines count {}", fileWriterService.getWritedLineCount());

    }

    private boolean stopWriting() {
        return broker.checkIfFlagSet(Flag.EXTRACTION_FINISHED)
                && broker.getCounterValue("processedLines") == broker.getCounterValue("extractedLines")
                && broker.getCounterValue("writedLines") == broker.getCounterValue("extractedLines");
    }
}
