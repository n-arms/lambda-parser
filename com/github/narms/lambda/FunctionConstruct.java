package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FunctionConstruct extends Construct{
    ArrayDeque<Object> arguments;
    ArrayDeque<Object> body;
    boolean onBody;
    public FunctionConstruct(){
        arguments = new ArrayDeque<Object>();
        body = new ArrayDeque<Object>();
        onBody = false;
    }
    public boolean isFull(){
        return body.size()>0;
    }

    @Override
    public void addObject(Object o){
        assert(o instanceof Token || o instanceof Expression);
        if(o instanceof Token && ((Token)o).getType().equals(TokenType.DOT)){
            onBody = true;
        }else{
            if (onBody){
                body.add(o);
            }else{
                arguments.add(o);
            }
        }
    }
    public List<Argument> getArguments(){
        List<Argument> output = new ArrayList<Argument>();
        ArrayDeque<Object> argumentHelper = this.arguments.clone();
        for(int i = 0; i<this.arguments.size(); i++)
        output.add((Argument)argumentHelper.pop());

        return output;
    }
    public Expression getBody(){
        assert(body.size()==1 && body.peek() instanceof Expression);
        return (Expression)body.peek();
    }

    @Override
    public Deque<Expression> parse(){
        return Parser.parse(this.body);
    }

    @Override
    public String toString(){
        return "λ"+arguments+"."+body;
    }

}
