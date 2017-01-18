package com.solanteq.assingment.services.impl;

import com.solanteq.assingment.services.ReaderService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class RecordGroupingServiceImplTest {
    @Test
    public void group() throws Exception {
        ReaderService<int[]> frs = (ReaderService<int[]>) Mockito.mock(ReaderService.class);
        Mockito.when(frs.read()).then(invocation -> new int[]{2, 3});
        RecordGroupingServiceImpl<int[]> groupingService = new RecordGroupingServiceImpl<>(frs, 3);
        Collection<int[]> group = groupingService.group();
        assertEquals(3, group.size());
        for (int[] ints : group) {
            assertArrayEquals(new int[]{2, 3}, ints);

        }
    }

}