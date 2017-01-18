package com.solanteq.assingment.services;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public interface LineMapper<I> {
    I map(String line, long lineNumber);
}
