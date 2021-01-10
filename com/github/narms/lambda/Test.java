package com.github.narms.lambda;

public class Test {
    public static void main (String[] args){
        Combinator.usingSKI();
        Expression identity = new Application(new Application(Combinator.get("S"), Combinator.get("K")), Combinator.get("S")).reduce();
        Application kite = new Application(Combinator.get("K"), identity);
        System.out.println(kite);
        System.out.println(kite.reduce());
    }
}