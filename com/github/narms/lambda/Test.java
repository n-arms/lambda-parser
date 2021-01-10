package com.github.narms.lambda;

import java.util.LinkedList;


public class Test {
    public static void main (String[] args){
        LinkedList<Argument> a1 = new LinkedList<Argument>();
        a1.add(new Argument("a"));
        a1.add(new Argument("b"));
        LinkedList<Argument> a2 = new LinkedList<Argument>();
        a2.add(new Argument("a"));
        a2.add(new Argument("b"));
        Function True = new Function(a1, a1.get(0));
        Function False = new Function(a2, a2.get(1));
        Expression TrueFalse = new Application(True, False);
        System.out.println(TrueFalse);
        TrueFalse = TrueFalse.reduce();
        System.out.println(TrueFalse);
        TrueFalse = new Application(TrueFalse, new Argument("c"));
        System.out.println(TrueFalse);
        TrueFalse = TrueFalse.reduce();
        System.out.println(TrueFalse);
    }
}