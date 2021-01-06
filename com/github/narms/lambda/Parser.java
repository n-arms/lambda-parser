package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parser {
    public static Deque<Expression> parse(Deque<Token> tokens){
        Deque<Object> struct = new ArrayDeque<Object>();
        while (tokens.size()>0){
            Token current = tokens.pop();
            if (current.getType().equals(TokenType.LEFTPAREN)){
                ParenConstruct build = new ParenConstruct();
                current = tokens.pop();
                while (!build.isFull()){
                    build.addObject(current);
                    if (!tokens.isEmpty())
                    current = tokens.pop();
                }
                struct.add(build.parse().peek());
            }
            else{
                struct.add(current);
            }
        }
        System.out.println("entering main parse with "+struct);
        Deque<Expression> output = new ArrayDeque<Expression>();
        while (struct.size()>0){
            Object current = struct.pop();
            if (current instanceof Token){
                Token currentToken = (Token)current;
                switch(currentToken.getType()){
                    case ARGUMENT:
                    if (output.peek() == null){
                        output.add(new Argument(currentToken.getValue()));
                    }else{
                        Expression last = output.pop();
                        output.add(new Application(last, new Argument(currentToken.getValue())));
                    }
                    case LEFTPAREN:
                    break;
                    case RIGHTPAREN:
                    break;
                    case LAMBDA:
                    FunctionConstruct build = new FunctionConstruct();
                    while (currentToken.getType() != TokenType.SPACE && struct.size()>0){
                        current = struct.pop();
                        build.addObject(current);
                    }
                    build.parse();
                    output.add(new Function(build));
                    break;
                    default:
                    break;
                }
            }else if (current instanceof Expression){
                System.out.println("entering non-tokeniezed parsing");
                if (output.peek() == null){
                    output.add((Expression)current);
                }else{
                    output.add(new Application(output.pop(), (Expression)current));
                }
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
