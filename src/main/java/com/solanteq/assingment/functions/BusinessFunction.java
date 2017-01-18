package com.solanteq.assingment.functions;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public interface BusinessFunction<I, O> {
    O apply(I ...argument);
}
