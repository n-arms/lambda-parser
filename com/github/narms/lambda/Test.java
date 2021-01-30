package com.github.narms.lambda;

import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main (String[] args){
        Argument a = new Argument("a");
        LinkedList<Argument> l = new LinkedList<Argument>();
        l.add(a);
        l.add(new Argument("b"));
        List<String> scope = new LinkedList<String>();
        scope.add(a.getName());
        scope.add(l.get(1).getName());
        Expression func = new Function(l, a);
        System.out.println(func);
        func = func.alphaReduce(scope);
        System.out.println(func);
    }
}