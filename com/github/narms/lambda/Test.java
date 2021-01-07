package com.github.narms.lambda;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Test {
    public static void main (String[] args){
        
        /*List<Expression> combinators = new ArrayList<Expression>();
        combinators.add(Parser.parse(Lexer.lex("λab.a")).peek()); //logical true
        combinators.add(Parser.parse(Lexer.lex("λcd.d")).peek()); //logical false
        combinators.add(Parser.parse(Lexer.lex("λfeg.fge")).peek()); //logical not

        System.out.println(combinators);*/
        Expression test = Parser.parse(Lexer.lex("(λab.a) c")).peek();
        System.out.println(test);
        System.out.println(test.reduce());

    }
}