package com.github.narms.lambda;

import java.util.LinkedList;

public class Function extends Expression{
    //where the last element of the arraylist is the first argument in the lamnbda
    private LinkedList<Argument> arguments; 
    private Expression body;
    public Function(LinkedList<Argument> arguments, Expression body){
        this.arguments = arguments;
        this.body = body;
    }

    @Override
    public Expression reduce() {
        this.body = this.body.reduce();
        if (this.body instanceof Function){
            this.arguments.addAll(((Function)this.body).getArguments());
     
            this.body = ((Function)this.body).getBody();
        }
        return this;
    }

    public LinkedList<Argument> getArguments(){
        return this.arguments;
    }

    public Expression getBody(){
        return this.body;
    }

    public Expression apply(Expression e){
        return this.defineArgument(this.arguments.get(0), e);
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer();
        output.append('λ');
        for (int i = 0; i<arguments.size(); i++){
            output.append(arguments.get(i).toString());
        }
        output.append('.');
        output.append(this.body.toString());
        return output.toString();

    }

    @Override
    public Expression defineArgument(Argument a, Expression e){
        for (int i = 0; i<this.arguments.size(); i++){
            if (this.arguments.get(i).getName().equals(a.getName())){
                this.arguments.remove(i);
                break;
            }
        }
        this.body = this.body.defineArgument(a, e);

        if (this.arguments.size() > 0){
            return this;
        }
        return this.body;
        
    }

    @Override
    public Expression getLeft(){
        return this;
    }

    @Override
    public boolean canReduce(Argument a){
        for (Argument i: this.arguments){
            if (a.getName().equals(i.getName())){
                return false;
            }
        }
        return this.body.canReduce(a);
    }
}
