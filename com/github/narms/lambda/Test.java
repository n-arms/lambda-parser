package com.github.narms.lambda;

import java.util.LinkedList;

public class Test {
    public static void main (String[] args){
        LinkedList<Argument> args1 = new LinkedList<Argument>();
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
        Expression func1 = new Function(args1, args1.get(1));
        Expression func2 = new Function(args2, args2.get(0));
        Expression func3 = new Function(args3, args3.get(1));
        Expression func4 = new Function(args4, args4.get(1));

        Expression myAppli = new Application(func1, new Application(func2, func3));

        System.out.println(func1);
        //System.out.println(func1.defineArgument(new Argument("a"), new Argument("c")));
        System.out.println(func2);
        System.out.println(func3);
        System.out.println(myAppli);
        myAppli = myAppli.defineArgument(new Argument("a"), func4);
        System.out.println(myAppli);
        myAppli = myAppli.reduce();
        System.out.println(myAppli);
    }
}
