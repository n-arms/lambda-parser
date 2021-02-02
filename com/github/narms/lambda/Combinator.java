package com.github.narms.lambda;

import java.util.Map;
import java.util.HashMap;

public class Combinator {
    private static Map<String, Expression> combinators = new HashMap<String, Expression>();
    public static void define(String s, Expression e){
        combinators.put(s, e);
    }

    public static Expression get(String s){
        return combinators.get(s);
    }

    public static boolean inScope(String s){
        return combinators.containsKey(s);
    }
}
