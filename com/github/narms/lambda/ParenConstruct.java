package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.Deque;

public class ParenConstruct extends Construct{
    Deque<Object> rawContents;
    int leftParens;
    int rightParens;

    public ParenConstruct(){
        rawContents = new ArrayDeque<Object>();
        leftParens = 1;
    }

    @Override
    public void addObject(Object o){
        if (o instanceof Token){
            switch (((Token)o).getType()){
                case LEFTPAREN:
                leftParens++;
                break;
                case RIGHTPAREN:
                rightParens++;
                break;
                default:
            }
        } 
        rawContents.add(o);
    }

    @Override
    public String toString(){
        Deque<Object> stringCopy = new ArrayDeque<Object>();
        stringCopy.addAll(rawContents);
        StringBuffer output = new StringBuffer();
        while (stringCopy.size()>0){
            output.append(stringCopy.pop().toString());
        }
        return output.toString();
    }

    @Override
    public Deque<Expression> parse(){
        return Parser.parse(rawContents);
    }

    @Override
    public boolean isFull(){
        return leftParens == rightParens;
    }
}
