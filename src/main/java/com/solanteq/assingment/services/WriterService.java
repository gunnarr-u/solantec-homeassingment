package com.solanteq.assingment.services;

import java.io.IOException;
import java.util.List;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public interface WriterService<O> {
    long write(List<O> output) throws IOException;
    void open() throws Exception;
    void close();
    long getWritedLineCount();
}
