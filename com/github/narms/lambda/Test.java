package com.github.narms.lambda;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

public class Test {
    public static void main (String[] args){
        /*
        List<Expression> combinators = new ArrayList<Expression>();
        combinators.add(Parser.parse(Lexer.lex("λab.a")).peek()); //logical true
        combinators.add(Parser.parse(Lexer.lex("λcd.d")).peek()); //logical false
        combinators.add(Parser.parse(Lexer.lex("λfeg.fge")).peek()); //logical not

        System.out.println(combinators);*/
        Expression test = Parser.parse(Lexer.lex("(λab.a) b c")).peek();
        System.out.println(test);
        System.out.println(test.reduce());
        /*
        Argument a1 = new Argument("a");
        Argument a2 = new Argument("a");
        Deque<Argument> test = new ArrayDeque<Argument>();
        test.add(a1);
        Argument.undefine(test);
        Argument a3 = new Argument("a");

        System.out.println(a1+", "+a2+", "+a3);
        System.out.println(a1.equals(a2));
        System.out.println(a1.equals(a3));*/
    }
}