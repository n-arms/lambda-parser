package com.github.narms.lambda;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class Combinator {
    private static Map<String, Expression> combinators = new HashMap<String, Expression>();
    public static void assign(String s, Expression e){
        combinators.put(s, e);
    }
    public static Expression get(String s){
        return combinators.get(s).copy();
    }
    public static void delete(String s){
        combinators.remove(s);
    }
    public static void usingSKI(){
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("x"));
        combinators.put("I", new Function(a1, a1.get(0)));
        
        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("x"));
        a2.add(new Argument("y"));
        combinators.put("K", new Function(a2, a2.get(0)));

        LinkedList<Argument> a3 = new LinkedList<Argument>();
        a3.add(new Argument("x"));
        a3.add(new Argument("y"));
        a3.add(new Argument("z"));
        combinators.put("S", new Function(a3, new Application(new Application(a3.get(0), a3.get(2)), new Application(a3.get(1), a3.get(2)))));
    }
}
