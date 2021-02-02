package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Test {
    public static void main (String[] args){
        /*
        Scanner s = new Scanner(System.in);
        System.out.print("> ");
        while (!s.hasNext("stop")){
            try{
                Deque<Expression> line = Parser.parse(Lexer.lex(s.nextLine()));
                if (line.size() > 1){
                    System.out.print("Parse failed: could not reduce "+line+" to one root tree\n> ");
                }else{
                    Expression e = line.pop();
                    e = e.normalize();
                    e = e.format();
                    System.out.print(e+"\n> ");
                }
            }catch (Exception e){
                System.out.println("error\n> ");
            }
        }
        s.close();*/
        ArrayDeque<Object> line = Lexer.lex("Abc");
        System.out.println(line);
        Expression myExp = Parser.parse(line).peek();
        Combinator.define("A", new Argument("q"));
        System.out.println(myExp);
        myExp = myExp.normalize();
        System.out.println(myExp);
    }
        
}