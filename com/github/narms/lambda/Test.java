package com.github.narms.lambda;

import java.util.ArrayList;

public class Test {
    public static void main (String[] args){
        Combinator.using("recursion");
        Combinator.using("SKI");
        Expression a1 = new Application(Combinator.get("K"), Combinator.get("K"));
        System.out.println(a1);
        System.out.println(a1.reduce(new ArrayList<Argument>()));
        System.out.println(Combinator.get("M").reduce(new ArrayList<Argument>()));
    }
}