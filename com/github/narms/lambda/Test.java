package com.github.narms.lambda;

import java.util.ArrayList;

public class Test {
    public static void main (String[] args){
        Combinator.using("recursion");
        Combinator.using("SKI");
        System.out.println(Combinator.get("M"));
        System.out.println(Combinator.get("M").reduce(new ArrayList<Argument>()));
        Expression infinity = new Application(Combinator.get("M"), Combinator.get("M"));
        System.out.println(infinity);
        
    }

    public static int infiniteRecusrion(){
        return infiniteRecusrion();
    }
}