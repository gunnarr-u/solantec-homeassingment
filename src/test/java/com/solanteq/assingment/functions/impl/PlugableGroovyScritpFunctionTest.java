package com.solanteq.assingment.functions.impl;

import org.junit.Test;
import org.springframework.scripting.support.StaticScriptSource;

import static org.junit.Assert.assertEquals;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class PlugableGroovyScritpFunctionTest {

    private PlugableGroovyScritpFunction<Integer, Integer> function;

    @Test
    public void testApplyGroovyScript() throws Exception {
        function = new PlugableGroovyScritpFunction<>(new StaticScriptSource(
                "ints = binding.getVariable(\"item\");\n" +
                "Integer result = ints[0] - ints[1];"));
        assertEquals(Integer.valueOf(2), function.apply(4, 2));
    }

}