package com.solanteq.assingment.services;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public interface RecordGroupingService<I> {
    Collection<I> group() throws IOException;
}
