package com.solanteq.assingment.services.impl;

import com.solanteq.assingment.services.WriterService;
import org.springframework.core.io.PathResource;
import org.springframework.util.Assert;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class FileWriterService<O> implements WriterService<O> {
    private PathResource dest;
    private BufferedWriter writer;

    public long getWritedLineCount() {
        return writed;
    }
    private long writed = 0;

    public FileWriterService(PathResource dest) {
        this.dest = dest;
    }




    @Override
    public long write(List<O> output) throws IOException {
        Assert.notNull(writer, "Writer should be opened before write");
        if (output == null) return writed;
        for (O o : output) {
            writer.write(String.valueOf(o));
            writer.newLine();
            writed++;
        }
        writer.flush();
        return writed;
    }

    @Override
    public void open() throws Exception {
        Assert.notNull(dest, "Destination should be set before opening writer");
        writer = new BufferedWriter(new OutputStreamWriter(dest.getOutputStream()));
    }

    @Override
    public void close() {
        if (null != writer) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
