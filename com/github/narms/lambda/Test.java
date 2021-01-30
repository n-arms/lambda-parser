package com.github.narms.lambda;

import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main (String[] args){
        LinkedList<Argument> argsA = new LinkedList<Argument>();
        argsA.add(new Argument("a"));
        argsA.add(new Argument("b"));
        LinkedList<Argument> argsB = new LinkedList<Argument>();
        argsB.add(new Argument("a"));
        argsB.add(new Argument("b"));
        Expression f1 = new Function(argsB, new Argument("a"));
        Expression f2 = new Function(argsA, f1);
        System.out.println(f2);
        f2 = f2.format();
        System.out.println(f2);
    }
}