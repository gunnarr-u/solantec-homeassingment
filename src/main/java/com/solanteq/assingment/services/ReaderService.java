package com.solanteq.assingment.services;

import java.io.IOException;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public interface ReaderService<I> {
    I read() throws IOException;
    void open() throws Exception;
    void close() throws Exception;
}
