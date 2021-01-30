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
        Function f1 = new Function(argsB, new Argument("a"));
        Function f2 = new Function(argsA, new Argument("b"));
        System.out.println(f1+"\n"+f2);
        f1.alphaReduce(f2.bound());
        System.out.println("\n"+f1+"\n"+f2);
    }
}