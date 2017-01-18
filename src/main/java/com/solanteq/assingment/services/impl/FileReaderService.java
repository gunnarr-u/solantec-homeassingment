package com.solanteq.assingment.services.impl;

import com.solanteq.assingment.services.LineMapper;
import com.solanteq.assingment.services.ReaderService;
import org.springframework.core.io.PathResource;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class FileReaderService<I> implements ReaderService<I> {
    private LineMapper<I> lineMapper;
    private PathResource source;
    private BufferedReader reader;

    public long getReadedLineCount() {
        return readedLineCounter;
    }

    private long readedLineCounter = 0;

    public FileReaderService(PathResource source, LineMapper<I> lineMapper) {
        this.source = source;
        this.lineMapper = lineMapper;
    }




    @Override
    public I read() throws IOException {
        Assert.notNull(reader, "Reader should be opened before read");
        String line = reader.readLine();
        if (line == null) return null;

        long lineNumber = ++readedLineCounter;
        return lineMapper.map(line, lineNumber);
    }

    @Override
    public void open() throws Exception {
        Assert.notNull(source, "Source should be set before opening reader");
        reader = new BufferedReader(new InputStreamReader(source.getInputStream()));
    }

    @Override
    public void close() throws Exception {
        readedLineCounter = 0;
        if (null != reader) {
            reader.close();
        }
    }
}
