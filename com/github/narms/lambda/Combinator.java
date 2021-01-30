package com.github.narms.lambda;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Combinator {
    private static Map<String, Expression> combinators = new HashMap<String, Expression>();

    private static Map<String, Expression> SKI(){
        Map<String, Expression> output = new HashMap<String, Expression>();
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("x"));
        output.put("I", new Function(a1, a1.get(0)));
            
        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("x"));
        a2.add(new Argument("y"));
        output.put("K", new Function(a2, a2.get(0)));

        LinkedList<Argument> a3 = new LinkedList<Argument>();
        a3.add(new Argument("x"));
        a3.add(new Argument("y"));
        a3.add(new Argument("z"));
        output.put("S", new Function(a3, new Application(new Application(a3.get(0), a3.get(2)), new Application(a3.get(1), a3.get(2)))));
        return output;
    }

    private static Map<String, Expression> BCKW(){
        Map<String, Expression> output = new HashMap<String, Expression>();
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("x"));
        a1.add(new Argument("y"));
        a1.add(new Argument("z"));
        output.put("B", new Function(a1, new Application(a1.get(0), new Application(a1.get(1), a1.get(2)))));

        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("x"));
        a2.add(new Argument("y"));
        a2.add(new Argument("z"));
        output.put("C", new Function(a2, new Application(new Application(a2.get(0), a2.get(2)), a2.get(1))));

        LinkedList<Argument> a3 = new LinkedList<Argument>();
        a3.add(new Argument("x"));
        a3.add(new Argument("y"));
        output.put("K", new Function(a3, a3.get(0)));

        LinkedList<Argument> a4 = new LinkedList<Argument>();
        a4.add(new Argument("x"));
        a4.add(new Argument("y"));
        output.put("W", new Function(a4, new Application(new Application(a4.get(0), a4.get(1)), a4.get(1))));
        return output;
    }

    private static Map<String, Expression> recursion(){
        Map<String, Expression> output = new HashMap<String, Expression>();
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("x"));
        output.put("M", new Function(a1, new Application(a1.get(0), a1.get(0))));

        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("f"));
        LinkedList<Argument> a3 = new LinkedList<Argument>();
        a3.add(new Argument("x"));
        LinkedList<Argument> a4 = new LinkedList<Argument>();
        a4.add(new Argument("x"));
        output.put("Y", new Function(a2, new Application(new Function(a3, new Application(a2.get(0), new Application(a3.get(0), a3.get(0)))),
        new Function(a4, new Application(a2.get(0), new Application(a4.get(0), a4.get(0)))))));
        return output;
    }

    private static Map<String, Expression> churchBool(){
        Map<String, Expression> output = new HashMap<String, Expression>();
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("x"));
        a1.add(new Argument("y"));
        output.put("T", new Function(a1, a1.get(0)));

        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("x"));
        a2.add(new Argument("y"));
        output.put("F", new Function(a2, a2.get(1)));

        LinkedList<Argument> a3 = new LinkedList<Argument>();
        a3.add(new Argument("f"));
        a3.add(new Argument("a"));
        a3.add(new Argument("b"));
        output.put("NOT", new Function(a3, new Application(new Application(a3.get(0), a3.get(2)), a3.get(1))));

        LinkedList<Argument> a4 = new LinkedList<Argument>();
        a4.add(new Argument("a"));
        a4.add(new Argument("b"));
        output.put("EQUAL", new Function(a4, new Application(new Application(a4.get(0), a4.get(1)), new Application(new Application(a4.get(1), output.get("F")), output.get("T")))));

        LinkedList<Argument> a5 = new LinkedList<Argument>();
        a5.add(new Argument("a"));
        a5.add(new Argument("b"));
        output.put("AND", new Function(a5, new Application(new Application(a5.get(0), a5.get(1)), output.get("F"))));

        LinkedList<Argument> a6 = new LinkedList<Argument>();
        a6.add(new Argument("a"));
        a6.add(new Argument("b"));
        output.put("OR", new Function(a6, new Application(new Application(a6.get(0), output.get("T")), a6.get(1))));
        return output;
    }

    private static Map<String, Expression> churchNum(){
        Map<String, Expression> output = new HashMap<String, Expression>();

        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("f"));
        a1.add(new Argument("a"));
        output.put("0", new Function(a1, a1.get(1)));

        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("n"));
        a2.add(new Argument("f"));
        a2.add(new Argument("a"));
        output.put("+", new Function(a2, new Application(a2.get(1), new Application(new Application(a2.get(0), a2.get(1)), a2.get(2)))));

        ArrayList<LinkedList<Argument>> nArgs = new ArrayList<LinkedList<Argument>>();
        for (int i = 1; i<5; i++){
            nArgs.add(new LinkedList<Argument>());
            nArgs.get(nArgs.size()-1).add(new Argument("f"));
            nArgs.get(nArgs.size()-1).add(new Argument("a"));
            output.put(String.valueOf(i), new Application(output.get("+"), output.get(String.valueOf(i-1))));
        }
        return output;
    }

    public static void assign(String s, Expression e){
        combinators.put(s, e);
    }
    public static Expression get(String s){
        return combinators.get(s).copy().reduce(new ArrayList<Argument>());
    }
    public static void delete(String s){
        combinators.remove(s);
    }
    public static void using(String namespace){
        switch (namespace){
            case "SKI":
            combinators.putAll(SKI());
            case "BCKW":
            combinators.putAll(BCKW());
            case "recursion":
            combinators.putAll(recursion());
            case "boolean":
            combinators.putAll(churchBool());
            case "num":
            combinators.putAll(churchNum());
        }
    }
}