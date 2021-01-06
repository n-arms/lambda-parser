package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parser {
    public static Deque<Expression> parse(Deque<Token> struct){
        Deque<Expression> output = new ArrayDeque<Expression>();
        while (struct.size()>0){
            Token current = struct.pop();
            System.out.println(current);

            switch(current.getType()){
                case ARGUMENT:
                if (output.peek() == null){
                    output.add(new Argument(current.getValue()));
                }else{
                    Expression last = output.pop();
                    output.add(new Application(last, new Argument(current.getValue())));
                }
                case LEFTPAREN:
                break;
                case RIGHTPAREN:
                break;
                case LAMBDA:
                FunctionConstruct build = new FunctionConstruct();
                while (current.getType() != TokenType.SPACE && struct.size()>0){
                    current = struct.pop();
                    build.addObject(current);
                }
                build.parse();
                output.add(new Function(build));
                break;
                default:
                break;
            }
        }
        return output;
    }
    public static Deque<Expression> parseObj(Deque<Object> struct){
        Deque<Token> converter = new ArrayDeque<Token>();
        while (!struct.isEmpty()){
            converter.add((Token)struct.pop());
        }
        return Parser.parse(converter);
    }
}
