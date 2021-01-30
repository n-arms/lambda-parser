package com.github.narms.lambda;

import java.util.LinkedList;

public class Test {
    public static void main (String[] args){
        LinkedList<Argument> argsA = new LinkedList<Argument>();
        argsA.add(new Argument("a"));
        argsA.add(new Argument("b"));
        LinkedList<Argument> argsB = new LinkedList<Argument>();
        argsB.add(new Argument("x"));

        Expression k = new Function(argsA, new Argument("a"));
        Expression m = new Function(argsB, new Application(new Argument("x"), new Argument("x")));
        Expression a = new Application(m, k);
        System.out.println(k);
        System.out.println(m);
        System.out.println(a);
        a = a.normalize();
        System.out.println(a);
        a = a.format();
        System.out.println(a);
    }
}