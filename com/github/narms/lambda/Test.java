package com.github.narms.lambda;

import java.util.LinkedList;

public class Test {
    public static void main (String[] args){
        LinkedList<Argument> args1 = new LinkedList<Argument>();
        args1.add(new Argument("f"));
        args1.add(new Argument("a"));
        args1.add(new Argument("b"));

        LinkedList<Argument> args2 = new LinkedList<Argument>();
        args2.add(new Argument("x"));
        args2.add(new Argument("y"));

        LinkedList<Argument> args3 = new LinkedList<Argument>();
        args3.add(new Argument("i"));
        args3.add(new Argument("j"));

        LinkedList<Argument> args4 = new LinkedList<Argument>();
        args4.add(new Argument("p"));
        args4.add(new Argument("q"));

        LinkedList<Argument> args5 = new LinkedList<Argument>();
        args5.add(new Argument("m"));
        args5.add(new Argument("n"));

        LinkedList<Argument> args6 = new LinkedList<Argument>();
        args6.add(new Argument("z"));

        Expression identity = new Function(args6, args6.get(0)); // I x = x
        Expression kestrel = new Function(args5, args5.get(0));  // K a b = a, also known as logical true 
        Expression kite = new Function(args2, args2.get(1));     // KI a b = b, also known as logical false
        Expression cardinal = new Function(args1, new Application(new Application(args1.get(0), args1.get(2)), args1.get(2)));
        //C f a b = f b a, also know as functional composition, logical not
        
        Expression myAppli = new Application(cardinal, kestrel);

        System.out.println(kestrel);
        System.out.println(kite);
        System.out.println(cardinal);
        System.out.println(identity);
        
        System.out.println(myAppli);
        myAppli = myAppli.reduce();
        System.out.println(myAppli);

    }
}
