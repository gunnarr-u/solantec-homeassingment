package com.solanteq.assingment.services.impl;


import com.solanteq.assingment.services.ReaderService;
import com.solanteq.assingment.services.RecordGroupingService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class RecordGroupingServiceImpl<I> implements RecordGroupingService<I> {

    final ReaderService<I> readerService;
    final int groupSize;

    public RecordGroupingServiceImpl(ReaderService<I> readerService, int groupSize) {
        this.readerService = readerService;
        this.groupSize = groupSize;
    }

    @PostConstruct
    public void init() throws Exception {
        readerService.open();
    }

    @Override
    public Collection<I> group() throws IOException {
        List<I> group = new ArrayList<I>(groupSize);
        I item;
        while (group.size() < groupSize && null != (item = readerService.read())) {
            group.add(item);
        }
        return group;
    }
}
