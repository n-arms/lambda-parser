package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;

public class Parser {
    public static Deque<Expression> parse(Deque<Object> objects){
        Deque<Object> struct = new ArrayDeque<Object>();
        while (objects.size()>0){
            Object currentObj = objects.pop();
            if (currentObj instanceof Token){
                Token current = (Token)currentObj;
                if (current.getType().equals(TokenType.LEFTPAREN)){
                    ParenConstruct build = new ParenConstruct();
                    currentObj = objects.pop();
                    while (!build.isFull()){
                        build.addObject(currentObj);
                        if (!objects.isEmpty())
                        currentObj = objects.pop();
                    }
                    struct.add(build.parse().peek());
                }
                else{
                    struct.add(current);
                }
            }else{
                struct.add(currentObj);
            }
        }
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
                    case VARIABLE:
                    output.add(new Variable(currentToken.getValue()));
                    break;
                    default:
                    break;
                }
            }else if (current instanceof Expression){
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
        return Parser.parse(struct);
    }
}
