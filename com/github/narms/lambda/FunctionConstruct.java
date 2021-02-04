package com.github.narms.lambda;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class FunctionConstruct extends Construct{
    ArrayDeque<Object> arguments;
    ArrayDeque<Object> body;
    Expression completeBody;
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
        if(!onBody && o instanceof Token && ((Token)o).getType().equals(TokenType.DOT)){
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
        assert(completeBody != null);
        return completeBody;
    }

    @Override
    public Deque<Expression> parse(){
        Deque<Expression> output = Parser.parseObj(body);

        for (int i = 0; i<arguments.size(); i++){
            Token current = (Token)arguments.pop();
            arguments.add(new Argument(current.getValue()));
        }
        
        
        assert !output.isEmpty() && output.size()==1;
        completeBody = output.peek();
        return output;
    }

    @Override
    public String toString(){
        return "λ"+arguments+"."+body;
    }
}
