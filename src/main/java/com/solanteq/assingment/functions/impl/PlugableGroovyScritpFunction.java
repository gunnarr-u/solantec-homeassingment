package com.solanteq.assingment.functions.impl;

import com.solanteq.assingment.functions.BusinessFunction;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.groovy.GroovyScriptEvaluator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman_Mashchenko on 1/16/2017.
 */
public class PlugableGroovyScritpFunction<I, O> implements BusinessFunction<I, O> {

    ScriptEvaluator scriptEvaluator = new GroovyScriptEvaluator();
    private ScriptSource script;

    public PlugableGroovyScritpFunction(ScriptSource script) {
        this.script = script;
    }


    @Override
    public O apply(I ...arguments) {
        Map<String, Object> argumentsMap = new HashMap<>();
        argumentsMap.put("item", arguments);
        return (O) scriptEvaluator.evaluate(script, argumentsMap );
    }
}
